package com.tw.api_maintenance.before;

import java.util.List;

public class TeamBuildingPackageItem {
    private Long id;
    private Long packageId;
    private List<ActivityItem> activityItems;

    public TeamBuildingPackageItem(Long id, Long packageId, List<ActivityItem> activityItems){
        this.id = id;
        this.packageId = packageId;
        this.activityItems = activityItems;
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
}
