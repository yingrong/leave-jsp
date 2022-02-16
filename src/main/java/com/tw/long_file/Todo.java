package com.tw.long_file;

import java.util.UUID;

public class Todo {
    private String id;
    private String title;
    private Boolean completed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Todo() {
    }

    public Todo(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.completed = false;
    }

    public Todo(String id, String title, Boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }
}
