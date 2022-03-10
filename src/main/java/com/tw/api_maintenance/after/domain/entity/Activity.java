package com.tw.api_maintenance.after.domain.entity;

public class Activity {
    private Long id;
    private String name;

    public Activity(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
