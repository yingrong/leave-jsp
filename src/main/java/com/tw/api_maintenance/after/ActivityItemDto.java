package com.tw.api_maintenance.after;

public class ActivityItemDto {
    private Long id;
    private Long activityId;
    private String name;
    private Integer count;
    private Boolean selected;

    public ActivityItemDto(Long id, Long activityId, String name, Integer count, Boolean selected) {
        this.id = id;
        this.activityId = activityId;
        this.name = name;
        this.count = count;
        this.selected = selected;
    }

    public Long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public Boolean getSelected() {
        return selected;
    }
}
