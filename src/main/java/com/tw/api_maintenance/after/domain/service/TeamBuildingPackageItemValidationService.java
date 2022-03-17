package com.tw.api_maintenance.after.domain.service;

import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.*;
import com.tw.api_maintenance.after.domain.service.teambuilding_package_item_validator.*;

import java.util.Arrays;
import java.util.List;

public class TeamBuildingPackageItemValidationService {
    ITeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    ITeamBuildingPackageRepository teamBuildingPackageRepository;
    IActivityRepository activityRepository;
    IActivityMutexRepository activityMutexRepository;
    IActivityDependentRepository activityDependentRepository;

    public TeamBuildingPackageItemValidationService(ITeamBuildingPackageItemRepository teamBuildingPackageItemRepository, ITeamBuildingPackageRepository teamBuildingPackageRepository, IActivityRepository activityRepository, IActivityMutexRepository activityMutexRepository, IActivityDependentRepository activityDependentRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
        this.activityMutexRepository = activityMutexRepository;
        this.activityDependentRepository = activityDependentRepository;
    }

    public void validate(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        List<TeamBuildingPackageItemValidator> validators = Arrays.asList(
                new CountValidator(),
                new ReliedActivitySelectValidator(activityDependentRepository, teamBuildingPackageRepository, activityRepository),
                new AlreadySelectedLastTimeValidator(teamBuildingPackageItemRepository, activityRepository),
                new MutexActivityValidator(activityMutexRepository, teamBuildingPackageRepository, activityRepository)
        );

        for (TeamBuildingPackageItemValidator validator : validators) {
            validator.validate(teamBuildingPackageItem, activityItemId, count);
        }
    }
}
