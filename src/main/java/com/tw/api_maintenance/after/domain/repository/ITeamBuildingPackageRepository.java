package com.tw.api_maintenance.after.domain.repository;

import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;

public interface ITeamBuildingPackageRepository {
    TeamBuildingPackage findById(Long id);
}
