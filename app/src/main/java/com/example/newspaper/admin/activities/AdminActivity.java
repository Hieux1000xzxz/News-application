package com.example.newspaper.admin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.newspaper.R;
import com.example.newspaper.databinding.ActivityAdminBinding;
import com.example.newspaper.databinding.ActivityMainBinding;
import com.example.newspaper.user.activities.MainActivity;
import com.example.newspaper.user.activities.SearchActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class AdminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;
    private Dialog dialog;
    private MaterialButton confirm, cancel;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_report, R.id.nav_managerUser, R.id.nav_manageArticle, R.id.nav_manageCategory)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setDialog();
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                dialog.show();
                DrawerLayout drawerLayout = binding.drawerLayout;
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                DrawerLayout drawerLayout = binding.drawerLayout;
                drawerLayout.closeDrawer(GravityCompat.START);
                return handled;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logOut(){
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void setDialog(){
        dialog = new Dialog(AdminActivity.this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_custom_dialog);
        dialog.setCancelable(false);

        confirm = dialog.findViewById(R.id.confirm);
        cancel = dialog.findViewById(R.id.cancel);
        message = dialog.findViewById(R.id.message);
        message.setText("Bạn có chắc muốn xóa hêts lịch sử tìm kiếm không");

        confirm.setOnClickListener(v1 -> {
            logOut();
            dialog.dismiss();
            startActivity(new Intent(this, MainActivity.class));
        });
        cancel.setOnClickListener(v1 -> dialog.dismiss());
    }
}