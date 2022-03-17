package com.tw.api_maintenance.after.domain.service.teambuilding_package_item_validator;

import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.AlreadySelectedLastTimeErrorDetail;
import com.tw.api_maintenance.after.domain.error_handling.ErrorName;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.IActivityRepository;
import com.tw.api_maintenance.after.domain.repository.ITeamBuildingPackageItemRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AlreadySelectedLastTimeValidator implements TeamBuildingPackageItemValidator{
    private ITeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    private IActivityRepository activityRepository;

    public AlreadySelectedLastTimeValidator(ITeamBuildingPackageItemRepository teamBuildingPackageItemRepository, IActivityRepository activityRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public void validate(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        ActivityItem activityItem = teamBuildingPackageItem.getActivityItemByItemId(activityItemId);
        TeamBuildingPackageItem lastPackageItem = teamBuildingPackageItemRepository.findLastCompleted();

        if (lastPackageItem != null && Objects.equals(lastPackageItem.getPackageId(), teamBuildingPackageItem.getPackageId()) &&
                lastPackageItem.getActivityItems().stream()
                        .filter(ai -> ai.getSelected()).anyMatch(ai -> Objects.equals(ai.getActivityId(), activityItem.getActivityId()))) {
            List<Activity> activities = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId()));
            throw new SelectActivityException(ErrorName.AlreadySelectedLastTime,
                    new AlreadySelectedLastTimeErrorDetail(activityItem.getId(), activityItem.getActivityId(), activities.get(0).getName()));
        }
    }
}
