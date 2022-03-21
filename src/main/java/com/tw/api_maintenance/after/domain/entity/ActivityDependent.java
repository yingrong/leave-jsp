package com.tw.api_maintenance.after.domain.entity;

public class ActivityDependent {
    private final Long id;
    private final Long teamBuildingPackageId;
    private final Long activityId;
    private final Long dependentActivityId;

    public ActivityDependent(Long id, Long teamBuildingPackageId, Long activityId, Long dependentActivityId) {

        this.id = id;
        this.teamBuildingPackageId = teamBuildingPackageId;
        this.activityId = activityId;
        this.dependentActivityId = dependentActivityId;
    }

    public Long getTeamBuildingPackageId() {
        return teamBuildingPackageId;
    }

    public Long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Long getDependentActivityId() {
        return dependentActivityId;
    }
}
