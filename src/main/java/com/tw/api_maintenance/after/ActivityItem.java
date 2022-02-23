package com.tw.api_maintenance.after;

public class ActivityItem {
    private Long id;
    private Long activityId;
    private Boolean selected;
    private Integer count;

    public ActivityItem(Long id, Long activityId, Boolean selected,Integer count){
        this.id = id;
        this.activityId = activityId;
        this.selected = selected;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Integer getCount() {
        return count;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
