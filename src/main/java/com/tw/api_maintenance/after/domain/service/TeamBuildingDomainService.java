package com.tw.api_maintenance.after.domain.service;

import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.*;

import java.util.Objects;

public class TeamBuildingDomainService {
    IActivityDependentRepository activityDependentRepository;
    TeamBuildingPackageItemValidationService teamBuildingPackageItemValidationService;

    public TeamBuildingDomainService(TeamBuildingPackageItemValidationService teamBuildingPackageItemValidationService,
                                     IActivityDependentRepository activityDependentRepository) {
        this.activityDependentRepository = activityDependentRepository;
        this.teamBuildingPackageItemValidationService = teamBuildingPackageItemValidationService;
    }

    public void selectActivityItem(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        teamBuildingPackageItemValidationService.validate(teamBuildingPackageItem, activityItemId, count);
        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getId(), activityItemId)).findFirst().get();
        activityItem.setSelected(true);
        activityItem.setCount(count);
    }

    public Long unSelectActivityItem(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId) {
        ActivityItem activityItem = teamBuildingPackageItem.getActivityItemByItemId(activityItemId);
        Long unselectedDependentActivityId = null;
        Long dependentActivityId = activityDependentRepository.findByDependentId(teamBuildingPackageItem.getPackageId(), activityItem.getActivityId());
        if (dependentActivityId != null) {
            ActivityItem dependentActivityItem = teamBuildingPackageItem.getActivityItemByActivityId(dependentActivityId);
            if (dependentActivityItem.getSelected()) {
                dependentActivityItem.setSelected(false);
                dependentActivityItem.setCount(null);
                unselectedDependentActivityId = dependentActivityItem.getId();
            }
        }

        activityItem.setSelected(false);
        activityItem.setCount(null);
        return unselectedDependentActivityId;
    }
}
