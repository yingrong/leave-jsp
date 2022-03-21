package com.tw.api_maintenance.before;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivityMutexRepository {
    Map<Long, ActivityMutex> activityMutexData = new HashMap<>();

    public ActivityMutexRepository() {
        activityMutexData.put(1L, new ActivityMutex(1L, 10010L, 1L, 2L));
        activityMutexData.put(2L, new ActivityMutex(2L, 10010L, 2L, 1L));
        activityMutexData.put(3L, new ActivityMutex(3L, 10086L, 1L, 11L));
        activityMutexData.put(4L, new ActivityMutex(4L, 10086L, 11L, 1L));
    }

    public List<Long> findByMutexActivityIds(Long teamBuildingPackageId, Long activityId) {
        return activityMutexData.values().stream().filter(am -> am.getTeamBuildingPackageId() == teamBuildingPackageId
        && am.getActivityId() == activityId).map(am -> am.getMutexActivityId()).collect(Collectors.toList());
    }
}
