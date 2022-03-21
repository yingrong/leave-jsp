package com.tw.api_maintenance.after.domain.repository;

import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;

public interface ITeamBuildingPackageItemRepository {
    TeamBuildingPackageItem findById(Long id);
    void save(TeamBuildingPackageItem packageItem);
    TeamBuildingPackageItem findLastCompleted();
}
