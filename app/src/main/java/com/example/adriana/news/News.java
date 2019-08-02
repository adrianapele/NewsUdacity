package com.example.adriana.news;

/**
 * Created by Adriana on 7/21/2018.
 */

public class News {
    private String title;
    private String details;
    private String author;
    private String date;
    private String topic;
    private String webURL;

    public News(String title, String details, String author, String date, String topic, String webURL) {
        this.title = title;
        this.details = details;
        this.author = author;
        this.date = date;
        this.topic = topic;
        this.webURL = webURL;
    }

    public News(String title, String date, String topic, String webURL) {
        this.title = title;
        this.date = date;
        this.topic = topic;
        this.webURL = webURL;
    }

    public News(String title, String author, String date, String topic, String webUrl) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.topic = topic;
        this.webURL = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }
}
