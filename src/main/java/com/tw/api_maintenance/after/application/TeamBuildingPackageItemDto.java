package com.tw.api_maintenance.after.application;

import com.tw.api_maintenance.after.application.ActivityItemDto;

import java.util.Date;
import java.util.List;

public class TeamBuildingPackageItemDto {
    private Long id;
    private Long packageId;
    private String name;
    private List<ActivityItemDto> activityItemDtos;
    private Date date;
    private boolean isCompleted;

    public TeamBuildingPackageItemDto(Long id, Long packageId, String name, List<ActivityItemDto> activityItemDtos, Date date, boolean isCompleted) {
        this.id = id;
        this.packageId = packageId;
        this.name = name;
        this.activityItemDtos = activityItemDtos;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public List<ActivityItemDto> getActivityItemDtos() {
        return activityItemDtos;
    }

    public Long getId() {
        return id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
