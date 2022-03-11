package com.tw.api_maintenance.after.domain.repository;

public interface IActivityMutexRepository {
    Long findByMutexActivityId(Long teamBuildingPackageId, Long activityId);
}
