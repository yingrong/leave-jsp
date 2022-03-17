package com.tw.api_maintenance.after.domain.service.teambuilding_package_item_validator;

import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.ErrorName;
import com.tw.api_maintenance.after.domain.error_handling.MutexActivityErrorDetail;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.IActivityMutexRepository;
import com.tw.api_maintenance.after.domain.repository.IActivityRepository;
import com.tw.api_maintenance.after.domain.repository.ITeamBuildingPackageRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MutexActivityValidator implements TeamBuildingPackageItemValidator{


    private IActivityMutexRepository activityMutexRepository;
    private ITeamBuildingPackageRepository teamBuildingPackageRepository;
    private IActivityRepository activityRepository;

    public MutexActivityValidator(IActivityMutexRepository activityMutexRepository, ITeamBuildingPackageRepository teamBuildingPackageRepository, IActivityRepository activityRepository) {
        this.activityMutexRepository = activityMutexRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;

        this.activityRepository = activityRepository;
    }

    @Override
    public void validate(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getId(), activityItemId)).findFirst().get();
        Long mutexActivityId = activityMutexRepository.findByMutexActivityId(teamBuildingPackageItem.getPackageId(), activityItem.getActivityId());
        if (mutexActivityId != null) {
            ActivityItem mutexActivity = teamBuildingPackageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getActivityId(), mutexActivityId)).findFirst().get();
            if (mutexActivity.getSelected()) {
                String packageName = teamBuildingPackageRepository.findById(teamBuildingPackageItem.getPackageId()).getName();
                Map<Long, String> activityIdToName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), mutexActivityId))
                        .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));

                throw new SelectActivityException(ErrorName.MutexActivity, new MutexActivityErrorDetail(teamBuildingPackageItem.getId(), teamBuildingPackageItem.getPackageId(), packageName,
                        activityItem.getId(), activityItem.getActivityId(), activityIdToName.get(activityItem.getActivityId()),
                        mutexActivityId, activityIdToName.get(mutexActivityId)));
            }
        }
    }
}
