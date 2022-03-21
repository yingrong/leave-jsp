package com.tw.api_maintenance.before;

public class ActivityMutex {
    private final Long id;
    private final Long teamBuildingPackageId;
    private final Long activityId;
    private final Long mutexActivityId;

    ActivityMutex(Long id, Long teamBuildingPackageId, Long activityId, Long mutexActivityId) {

        this.id = id;
        this.teamBuildingPackageId = teamBuildingPackageId;
        this.activityId = activityId;
        this.mutexActivityId = mutexActivityId;
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

    public Long getMutexActivityId() {
        return mutexActivityId;
    }
}
