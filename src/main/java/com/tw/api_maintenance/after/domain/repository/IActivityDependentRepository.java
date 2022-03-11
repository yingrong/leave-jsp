package com.tw.api_maintenance.after.domain.repository;

public interface IActivityDependentRepository {
    Long findByDependentId(Long teamBuildingPackageId, Long activityId);

    Long findByReliedId(Long teamBuildingPackageId, Long activityId);
}
