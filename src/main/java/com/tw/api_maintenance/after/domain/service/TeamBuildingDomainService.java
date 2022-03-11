package com.tw.api_maintenance.after.domain.service;

import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.*;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.infrastructure.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TeamBuildingDomainService {
    TeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    TeamBuildingPackageRepository teamBuildingPackageRepository;
    ActivityRepository activityRepository;
    private ActivityMutexRepository activityMutexRepository;
    private ActivityDependentRepository activityDependentRepository;

    public TeamBuildingDomainService(TeamBuildingPackageItemRepository teamBuildingPackageItemRepository, TeamBuildingPackageRepository teamBuildingPackageRepository, ActivityRepository activityRepository, ActivityMutexRepository activityMutexRepository, ActivityDependentRepository activityDependentRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
        this.activityMutexRepository = activityMutexRepository;
        this.activityDependentRepository = activityDependentRepository;
    }

    public void selectActivityItem(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        validateCount(count);
        validateReliedActivitySelect(teamBuildingPackageItem, activityItemId);
        validateAlreadySelectedLastTime(teamBuildingPackageItem, activityItemId);
        validateMutexActivity(teamBuildingPackageItem, activityItemId);

        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getId(), activityItemId)).findFirst().get();
        activityItem.setSelected(true);
        activityItem.setCount(count);
    }

    private void validateAlreadySelectedLastTime(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId) throws SelectActivityException {
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

    private void validateReliedActivitySelect(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId) throws SelectActivityException {
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

    private void validateCount(Integer count) throws SelectActivityException {
        if (count < 1 || count > 50) {
            throw new SelectActivityException(ErrorName.NotInRange, new NotInRangeErrorDetail(count, 1, 50));
        }
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


    private void validateMutexActivity(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId) throws SelectActivityException {
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
