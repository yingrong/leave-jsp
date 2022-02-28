package com.tw.api_maintenance.after;

import java.util.Date;
import java.util.List;

public class TeamBuildingPackageItem {
    private Long id;
    private Long packageId;
    private List<ActivityItem> activityItems;
    private Date date;
    private boolean isCompleted;

    public TeamBuildingPackageItem(Long id, Long packageId, List<ActivityItem> activityItems){
        this(id, packageId, activityItems, new Date(), false);
    }

    public TeamBuildingPackageItem(Long id, Long packageId, List<ActivityItem> activityItems, Date date, boolean isCompleted){
        this.id = id;
        this.packageId = packageId;
        this.activityItems = activityItems;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public Long getPackageId() {
        return packageId;
    }

    public List<ActivityItem> getActivityItems() {
        return activityItems;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
