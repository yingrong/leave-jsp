package com.tw.api_maintenance.after.domain.service.teambuilding_package_item_validator;

import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.ErrorName;
import com.tw.api_maintenance.after.domain.error_handling.ReliedNotSelectedErrorDetail;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.IActivityDependentRepository;
import com.tw.api_maintenance.after.domain.repository.IActivityRepository;
import com.tw.api_maintenance.after.domain.repository.ITeamBuildingPackageRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ReliedActivitySelectValidator implements TeamBuildingPackageItemValidator{


    private IActivityDependentRepository activityDependentRepository;
    private ITeamBuildingPackageRepository teamBuildingPackageRepository;
    private IActivityRepository activityRepository;

    public ReliedActivitySelectValidator(IActivityDependentRepository activityDependentRepository, ITeamBuildingPackageRepository teamBuildingPackageRepository, IActivityRepository activityRepository) {

        this.activityDependentRepository = activityDependentRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public void validate(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        ActivityItem activityItem = teamBuildingPackageItem.getActivityItemByItemId(activityItemId);
        Long reliedId = activityDependentRepository.findByReliedId(teamBuildingPackageItem.getPackageId(), activityItem.getActivityId());
        if (reliedId != null) {
            ActivityItem reliedActivityItem = teamBuildingPackageItem.getActivityItemByActivityId(reliedId);
            if (!reliedActivityItem.getSelected()) {
                TeamBuildingPackage packageEntity = teamBuildingPackageRepository.findById(teamBuildingPackageItem.getPackageId());
                Map<Long, String> idToActivityName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), reliedId))
                        .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));
                throw new SelectActivityException(ErrorName.ReliedNotSelected, new ReliedNotSelectedErrorDetail(teamBuildingPackageItem.getId(), teamBuildingPackageItem.getPackageId(), packageEntity.getName(),
                        activityItem.getId(), activityItem.getActivityId(), idToActivityName.get(activityItem.getActivityId()),
                        reliedId, idToActivityName.get(reliedId)));
            }
        }
    }
}
