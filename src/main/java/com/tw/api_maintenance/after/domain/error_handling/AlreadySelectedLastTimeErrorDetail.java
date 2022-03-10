package com.tw.api_maintenance.after.domain.error_handling;

public class AlreadySelectedLastTimeErrorDetail extends ErrorDetail {

    private Long activityItemId;
    private Long activityId;
    private String name;

    public AlreadySelectedLastTimeErrorDetail(Long activityItemId, Long activityId, String name){
        this.activityItemId = activityItemId;
        this.activityId = activityId;
        this.name = name;
    }

    public Long getActivityItemId() {
        return activityItemId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public String getName() {
        return name;
    }
}


