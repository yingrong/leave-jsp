package com.tw.api_maintenance.after.application.service;

import com.tw.api_maintenance.after.application.dto.ActivityItemDto;
import com.tw.api_maintenance.after.application.dto.TeamBuildingPackageItemDto;
import com.tw.api_maintenance.after.domain.error_handling.*;
import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.application.error_handling.Error;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.service.TeamBuildingDomainService;
import com.tw.api_maintenance.after.infrastructure.*;

import java.util.*;
import java.util.stream.Collectors;

public class TeamBuildingService {
    private final TeamBuildingDomainService teamBuildingDomainService;
    TeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    TeamBuildingPackageRepository teamBuildingPackageRepository;
    ActivityRepository activityRepository;

    public TeamBuildingService(TeamBuildingPackageItemRepository teamBuildingPackageItemRepository, TeamBuildingPackageRepository teamBuildingPackageRepository, ActivityRepository activityRepository, ActivityMutexRepository activityMutexRepository, ActivityDependentRepository activityDependentRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
        this.teamBuildingDomainService = new TeamBuildingDomainService(teamBuildingPackageItemRepository, teamBuildingPackageRepository, activityRepository,
                activityMutexRepository, activityDependentRepository);
    }

    public TeamBuildingPackageItemDto queryTeamBuildingPackage(Long packageId) {
        TeamBuildingPackageItem teamBuildingPackageItem = teamBuildingPackageItemRepository.findById(packageId);
        TeamBuildingPackage teamBuildingPackage = teamBuildingPackageRepository.findById(teamBuildingPackageItem.getPackageId());
        List<Activity> activities = activityRepository.findByIds(teamBuildingPackageItem.getActivityItems().stream().map(item -> item.getActivityId()).collect(Collectors.toList()));
        Map<Long, Activity> idToActivity = activities.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));

        List<ActivityItemDto> activityItemDtos = teamBuildingPackageItem.getActivityItems().stream()
                .map(item -> new ActivityItemDto(item.getId(),
                        item.getActivityId(),
                        idToActivity.get(item.getActivityId()).getName(),
                        item.getCount(),
                        item.getSelected())).collect(Collectors.toList());

        return new TeamBuildingPackageItemDto(teamBuildingPackageItem.getId(),
                teamBuildingPackage.getId(),
                teamBuildingPackage.getName(),
                activityItemDtos,
                teamBuildingPackageItem.getDate(),
                teamBuildingPackageItem.isCompleted());

    }

    public Error<? extends ErrorDetail> selectActivityItem(Long teamBuildingPackageItemId, Long activityItemId, String requestCount) {
        Integer count = null;
        try {
            count = Integer.parseInt(requestCount);
        } catch (NumberFormatException ex) {
            return new Error<>(ErrorName.UnexpectedType.getCode(), ErrorName.UnexpectedType.getDescription(),
                    new UnexpectedTypeErrorDetail(requestCount, Integer.class.getName()));
        }

        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);

        try {
            teamBuildingDomainService.selectActivityItem(packageItem, activityItemId, count);
            teamBuildingPackageItemRepository.save(packageItem);
        } catch (SelectActivityException ex) {
            return new Error<>(ex.getErrorName().getCode(), ex.getErrorName().getDescription(), ex.getErrorDetail());
        }

        return null;
    }

    public Long unSelectActivityItem(Long teamBuildingPackageItemId, Long activityItemId) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);

        Long unselectedDependentActivityId = teamBuildingDomainService.unSelectActivityItem(packageItem, activityItemId);
        teamBuildingPackageItemRepository.save(packageItem);
        return unselectedDependentActivityId;
    }

}
