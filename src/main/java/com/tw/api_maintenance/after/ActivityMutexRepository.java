package com.tw.api_maintenance.after;

import java.util.*;
import java.util.stream.Collectors;

public class ActivityMutexRepository {
    Map<Long, ActivityMutex> activityMutexData = new HashMap<>();

    public ActivityMutexRepository() {
        activityMutexData.put(1L, new ActivityMutex(1L, 10010L, 1L, 2L));
        activityMutexData.put(2L, new ActivityMutex(2L, 10010L, 2L, 1L));
        activityMutexData.put(3L, new ActivityMutex(3L, 10086L, 1L, 11L));
        activityMutexData.put(4L, new ActivityMutex(4L, 10086L, 11L, 1L));
    }

    public Long findByMutexActivityId(Long teamBuildingPackageId, Long activityId) {
        //规定在一个团建包中，一个活动的互斥活动有0到1个
        return activityMutexData.values().stream().filter(am -> Objects.equals(am.getTeamBuildingPackageId(), teamBuildingPackageId)
                && Objects.equals(am.getActivityId(), activityId)).map(am -> am.getMutexActivityId()).findFirst().orElse(null);
    }
}
