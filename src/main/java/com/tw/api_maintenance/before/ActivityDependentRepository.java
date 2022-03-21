package com.tw.api_maintenance.before;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivityDependentRepository {
    Map<Long, ActivityDependent> activityDependentData = new HashMap<>();

    public ActivityDependentRepository() {
        activityDependentData.put(1L, new ActivityDependent(1L, 10010L, 1L, 5L));
    }

    public List<Long> findByDependentIds(Long teamBuildingPackageId, Long activityId) {
        return activityDependentData.values().stream().filter(am -> am.getTeamBuildingPackageId() == teamBuildingPackageId
        && am.getActivityId() == activityId).map(am -> am.getDependentActivityId()).collect(Collectors.toList());
    }

    public List<Long> findByReliedIds(Long teamBuildingPackageId, Long activityId) {
        return activityDependentData.values().stream().filter(am -> am.getTeamBuildingPackageId() == teamBuildingPackageId
                && am.getDependentActivityId() == activityId).map(am -> am.getActivityId()).collect(Collectors.toList());
    }
}
