package com.example.newsapplication.Model;

public class Notification {
    private int imageResource;
    private String content;
    private String timeAgo;
    private boolean isRead;

    public Notification(int imageResource, String content, String timeAgo, boolean isRead) {
        this.imageResource = imageResource;
        this.content = content;
        this.timeAgo = timeAgo;
        this.isRead = isRead;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
