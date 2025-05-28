package com.example.newspaper.user.activities;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newspaper.R;
import com.example.newspaper.auth.LoginActivity;
import com.example.newspaper.common.models.Comment;
import com.example.newspaper.common.models.Emotion;
import com.example.newspaper.common.models.ReadHistory;
import com.example.newspaper.common.models.SavedArticle;
import com.example.newspaper.common.models.TypeEmotion;
import com.example.newspaper.common.pojo.EmotionCount;
import com.example.newspaper.common.utils.DateTimeFormatterUtil;
import com.example.newspaper.user.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.user.adapters.CommentRecycleVIewAdapter;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.user.view_models.ArticleViewModel;
import com.example.newspaper.user.view_models.CommentViewModel;
import com.example.newspaper.user.view_models.EmotionViewModel;
import com.example.newspaper.user.view_models.NotificationViewModel;
import com.example.newspaper.user.view_models.ReadHistoryViewModel;
import com.example.newspaper.user.view_models.SavedArticleViewModel;
import com.example.newspaper.user.view_models.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends AppCompatActivity {
    private int id;
    private CommentRecycleVIewAdapter adapter, adapterMenu;
    private UserViewModel userViewModel;
    private RecyclerView listComment, listCommentInDialog, listRelatedArticle;
    private ArticleViewModel viewModel;
    private EmotionViewModel emotionViewModel;
    private CommentViewModel commentViewModel;
    private SavedArticleViewModel savedArticleViewModel;
    private ReadHistoryViewModel readHistoryViewModel;
    private NotificationViewModel notificationViewModel;
    private ImageView thumbnail, authorAvatar, like, creative, unique, heart, comment, send, save;
    private TextView category, title, authorName, summary, publishedAt, content, likeCount, creativeCount, uniqueCount, heartCount;
    private BottomSheetDialog commentsDialg;
    private Dialog dialog;
    private MaterialButton confirm, cancel;
    TextView message, commentCount, commentCountInDialog;
    EditText commentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article_detail);

        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        emotionViewModel = new ViewModelProvider(this).get(EmotionViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        savedArticleViewModel = new ViewModelProvider(this).get(SavedArticleViewModel.class);
        readHistoryViewModel = new ViewModelProvider(this).get(ReadHistoryViewModel.class);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        mapping();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = (int) prefs.getLong("userId", -1);

        id = getIntent().getIntExtra("articleId", -1);
        viewModel.upViewCount(id);
        viewModel.loadArticle(id);
        viewModel.getArticle().observe(this, article -> {
            if (article != null) {
                Glide.with(this)
                        .load(article.getArticle().getThumbnailUrl())
                        .into(thumbnail);

                title.setText(article.getArticle().getTitle());
                category.setText(article.getCategory().getName());
                summary.setText(article.getArticle().getSummary());
                content.setText(Html.fromHtml(article.getArticle().getContent(), Html.FROM_HTML_MODE_COMPACT));
                DateTimeFormatterUtil formater = new DateTimeFormatterUtil();
                publishedAt.setText(formater.format(article.getArticle().getPublishedAt()));

                loadRelatedArticle(article.getArticle().getCategoryId());

                userViewModel.getUserById(article.getArticle().getUserId()).observe(this, user -> {
                    authorName.setText(user.getFullName());
                    Glide.with(this)
                            .load(user.avatarUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(authorAvatar);
                });
            }
        });

        int notificationId = getIntent().getIntExtra("notificationId", -1);
        if(notificationId != -1) notificationViewModel.setIsRead(notificationId);

        setDialog();

        observeEmotionCounts(id);

        observeEmotion(userId, id);

        observeComments(id);

        setSaveButton(userId);

        findViewById(R.id.back_icon).setOnClickListener(v -> {
            finish();
        });

        setupCommentsDialog();

        comment.setOnClickListener(v -> {
            commentsDialg.show();

            send = commentsDialg.findViewById(R.id.icon_send);
            listCommentInDialog = commentsDialg.findViewById(R.id.list_menu_comment);
            commentText = commentsDialg.findViewById(R.id.comment_text_box);
            listCommentInDialog.setLayoutManager(new LinearLayoutManager(this));

            commentViewModel.getAllComment(id).observe(this, comments -> {
                adapterMenu = new CommentRecycleVIewAdapter(comments);
                listCommentInDialog.setAdapter(adapterMenu);
            });

            commentText.setOnClickListener(v1 -> {
                if(userId == -1) dialog.show();
            });

            send.setOnClickListener(v1 -> {
                if (userId == -1) dialog.show();
                else if (commentText.getText().toString().trim().length() < 2){
                    Toast.makeText(this, "Bình luận phải chứa ít nhất 1 ký tự", LENGTH_SHORT).show();
                }else {
                    commentViewModel.insert(Comment.builder()
                            .articleId(id)
                            .userId(userId)
                            .content(commentText.getText().toString())
                            .createdAt(Instant.now())
                            .build());

                    commentText.setText("");
                }
            });

            commentText.setOnEditorActionListener((v1, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if(userId == -1) dialog.show();
                    else {
                        commentViewModel.insert(Comment.builder()
                                        .articleId(id)
                                        .userId(userId)
                                        .content(commentText.getText().toString())
                                        .createdAt(Instant.now())
                                .build());

                        commentText.setText("");
                    }
                    return true;
                }
                return false;
            });
        });

        if(userId != -1){
            readHistoryViewModel.insert(ReadHistory.builder()
                            .articleId(id)
                            .userId(userId)
                            .readAt(Instant.now())
                    .build());
        }

        commentViewModel.count(id).observe(this, count -> commentCount.setText(String.format("BÌNH LUẬN (%d)", count)));
    }

    public void mapping(){
        thumbnail = findViewById(R.id.thumbnail);
        authorAvatar = findViewById(R.id.authorAvatar);

        category = findViewById(R.id.category);
        title = findViewById(R.id.title);
        authorName = findViewById(R.id.authorName);
        summary = findViewById(R.id.sumary);
        content = findViewById(R.id.content);
        publishedAt = findViewById(R.id.publishedAt);
        commentCount = findViewById(R.id.commentCount);

        like = findViewById(R.id.like_icon);
        creative = findViewById(R.id.creative_icon);
        unique = findViewById(R.id.unique_icon);
        heart = findViewById(R.id.heart_icon);
        comment = findViewById(R.id.comment_icon);
        save = findViewById(R.id.save_icon);

        likeCount = findViewById(R.id.likeCount);
        creativeCount = findViewById(R.id.creativeCount);
        uniqueCount = findViewById(R.id.uniqueCount);
        heartCount = findViewById(R.id.heartCount);

        listComment = findViewById(R.id.listComments);
        listComment.setLayoutManager(new LinearLayoutManager(this));

        listRelatedArticle = findViewById(R.id.list_related_article);
        listRelatedArticle.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setSaveButton(int userId){
        if(userId == -1){
            save.setOnClickListener(v -> dialog.show());
        }else{
            savedArticleViewModel.getSavedArticle(userId, id).observe(this, savedArticle -> {
                boolean isSaved = (savedArticle != null);

                int tintColor = isSaved ? Color.GREEN : Color.WHITE;
                save.setImageTintList(ColorStateList.valueOf(tintColor));

                save.setOnClickListener(v -> {
                    if (isSaved) {
                        savedArticleViewModel.delete(savedArticle);
                    } else {
                        savedArticleViewModel.insert(SavedArticle.builder()
                                .userId(userId)
                                .articleId(id)
                                .build());
                    }
                });
            });
        }
    }

    private void updateSelectedEmotionUI(TypeEmotion selectedType) {
        like.setImageTintList(ColorStateList.valueOf(
                selectedType == TypeEmotion.LIKE ? Color.GREEN : Color.WHITE));
        creative.setImageTintList(ColorStateList.valueOf(
                selectedType == TypeEmotion.CREATIVE ? Color.GREEN : Color.WHITE));
        unique.setImageTintList(ColorStateList.valueOf(
                selectedType == TypeEmotion.UNIQUE ? Color.GREEN : Color.WHITE));
        heart.setImageTintList(ColorStateList.valueOf(
                selectedType == TypeEmotion.HEARTFELT ? Color.GREEN : Color.WHITE));
    }

    private void observeEmotion(int userId, int articleId){
        emotionViewModel.getEmotionOfCurrentUser(userId, id).observe(this, emotion -> {
            if(emotion != null){
                switch (emotion.getType()){
                    case LIKE:
                        like.setImageTintList(ColorStateList.valueOf(Color.GREEN));
                        like.invalidate();
                        break;
                    case CREATIVE:
                        creative.setImageTintList(ColorStateList.valueOf(Color.GREEN));
                        creative.invalidate();
                        break;
                    case UNIQUE:
                        unique.setImageTintList(ColorStateList.valueOf(Color.GREEN));
                        unique.invalidate();
                        break;
                    case HEARTFELT:
                        heart.setImageTintList(ColorStateList.valueOf(Color.GREEN));
                        heart.invalidate();
                        break;
                }

                like.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else{
                        emotion.setType(TypeEmotion.LIKE);
                        emotionViewModel.updateEmotion(emotion);
                        updateSelectedEmotionUI(TypeEmotion.LIKE);
                    }

                });
                creative.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else {
                        emotion.setType(TypeEmotion.CREATIVE);
                        emotionViewModel.updateEmotion(emotion);
                        updateSelectedEmotionUI(TypeEmotion.CREATIVE);
                    }
                });
                unique.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else {
                        emotion.setType(TypeEmotion.UNIQUE);
                        emotionViewModel.updateEmotion(emotion);
                        updateSelectedEmotionUI(TypeEmotion.UNIQUE);
                    }
                });
                heart.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else {
                        emotion.setType(TypeEmotion.HEARTFELT);
                        emotionViewModel.updateEmotion(emotion);
                        updateSelectedEmotionUI(TypeEmotion.HEARTFELT);
                    }
                });
            }else{
                Emotion newEmotion = Emotion.builder()
                        .articleId(id)
                        .userId(userId)
                        .createdAt(LocalDate.now())
                        .build();
                like.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else{
                        newEmotion.setType(TypeEmotion.LIKE);
                        emotionViewModel.insertEmotion(newEmotion);
                    }
                });
                creative.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else {
                        newEmotion.setType(TypeEmotion.CREATIVE);
                        emotionViewModel.insertEmotion(newEmotion);
                    }
                });
                unique.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else {
                        newEmotion.setType(TypeEmotion.UNIQUE);
                        emotionViewModel.insertEmotion(newEmotion);
                    }
                });
                heart.setOnClickListener(v -> {
                    if(userId == -1) dialog.show();
                    else{
                        newEmotion.setType(TypeEmotion.HEARTFELT);
                        emotionViewModel.insertEmotion(newEmotion);
                    }
                });
            }
        });
    }

    private void observeComments(int articleId){
        commentViewModel.getComment(articleId).observe(this, comments -> {
            adapter = new CommentRecycleVIewAdapter(comments);
            listComment.setAdapter(adapter);
        });
    }

    private void observeEmotionCounts(int articleId) {
        emotionViewModel.getEmotionCounts(articleId).observe(this, emotionCounts -> {
            likeCount.setText("0");
            creativeCount.setText("0");
            uniqueCount.setText("0");
            heartCount.setText("0");

            for (EmotionCount count : emotionCounts) {
                switch (count.getType()) {
                    case LIKE:
                        likeCount.setText(String.valueOf(count.getCount()));
                        break;
                    case CREATIVE:
                        creativeCount.setText(String.valueOf(count.getCount()));
                        break;
                    case UNIQUE:
                        uniqueCount.setText(String.valueOf(count.getCount()));
                        break;
                    case HEARTFELT:
                        heartCount.setText(String.valueOf(count.getCount()));
                        break;
                }
            }
        });
    }

    private void setupCommentsDialog() {
        commentsDialg = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.menu_comment, null);
        commentsDialg.setContentView(dialogView);
        commentCountInDialog = commentsDialg.findViewById(R.id.commentCountInDialog);
        commentViewModel.count(id).observe(this, count -> commentCountInDialog.setText(String.format("Bình luận (%d)", count)));

        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> commentsDialg.dismiss());
    }

    public void setDialog(){
        dialog = new Dialog(ArticleDetailActivity.this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_custom_dialog);
        dialog.setCancelable(false);

        confirm = dialog.findViewById(R.id.confirm);
        cancel = dialog.findViewById(R.id.cancel);
        message = dialog.findViewById(R.id.message);
        message.setText("Vui lòng đăng nhập để sử dụng chức năng bình luận");

        confirm.setOnClickListener(v1 -> {
            Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        cancel.setOnClickListener(v1 -> dialog.dismiss());
    }

    public void loadRelatedArticle(int id){
        viewModel.loadRelatedArticle(id);
        viewModel.getRelatedArticles().observe(this, articles -> {
            List<ArticleViewItem> items = new ArrayList<>();
            articles.forEach(c -> {
                if(c.getArticle().getId() != id) items.add(new ArticleViewItem(c, ArticleViewItem.TypeDisplay.RELATED));
            });
            ArticleRecycleViewAdapter adapter = new ArticleRecycleViewAdapter(items);
            listRelatedArticle.setAdapter(adapter);
        });
    }
}