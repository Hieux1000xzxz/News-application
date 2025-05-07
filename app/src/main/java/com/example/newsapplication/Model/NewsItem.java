package com.example.newsapplication.Model;

public class NewsItem {
    private String time;
    private String category;
    private String title;
    private String description;

    public NewsItem(String time, String category, String title, String description) {
        this.time = time;
        this.category = category;
        this.title = title;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}