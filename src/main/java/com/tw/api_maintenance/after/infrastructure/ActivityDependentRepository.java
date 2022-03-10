package com.tw.api_maintenance.after.infrastructure;

import com.tw.api_maintenance.after.domain.entity.ActivityDependent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityDependentRepository {
    Map<Long, ActivityDependent> activityDependentData = new HashMap<>();

    public ActivityDependentRepository() {
        activityDependentData.put(1L, new ActivityDependent(1L, 10010L, 1L, 5L));
    }

    public Long findByDependentId(Long teamBuildingPackageId, Long activityId) {
        return activityDependentData.values().stream().filter(am -> Objects.equals(am.getTeamBuildingPackageId(), teamBuildingPackageId)
        && Objects.equals(am.getActivityId(), activityId)).map(am -> am.getDependentActivityId()).findFirst().orElse(null);
    }

    public Long findByReliedId(Long teamBuildingPackageId, Long activityId) {
        return activityDependentData.values().stream().filter(am -> Objects.equals(am.getTeamBuildingPackageId(), teamBuildingPackageId)
                && Objects.equals(am.getDependentActivityId(), activityId)).map(am -> am.getActivityId()).findFirst().orElse(null);
    }
}
