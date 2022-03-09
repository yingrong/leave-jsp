package com.tw.api_maintenance.after;

import java.util.*;
import java.util.stream.Collectors;

public class TeamBuildingService {
    TeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    TeamBuildingPackageRepository teamBuildingPackageRepository;
    ActivityRepository activityRepository;
    private ActivityMutexRepository activityMutexRepository;

    public TeamBuildingService(TeamBuildingPackageItemRepository teamBuildingPackageItemRepository, TeamBuildingPackageRepository teamBuildingPackageRepository, ActivityRepository activityRepository, ActivityMutexRepository activityMutexRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
        this.activityMutexRepository = activityMutexRepository;
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

        if(count < 1 || count > 50) {
            return new Error<>(ErrorName.NotInRange.getCode(), ErrorName.NotInRange.getDescription(),
                    new NotInRangeErrorDetail(requestCount, 1, 50));
        }


        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();

        TeamBuildingPackageItem lastPackageItem = teamBuildingPackageItemRepository.findLastCompleted();

        if (lastPackageItem != null && Objects.equals(lastPackageItem.getPackageId(), packageItem.getPackageId()) &&
                lastPackageItem.getActivityItems().stream()
                        .filter(ai -> ai.getSelected()).anyMatch(ai -> ai.getActivityId() == activityItem.getActivityId())) {
            List<Activity> activities = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId()));
            return new Error<>(ErrorName.AlreadySelectedLastTime.getCode(),
                    ErrorName.AlreadySelectedLastTime.getDescription(),
                    new AlreadySelectedLastTimeErrorDetail(activityItem.getId(), activityItem.getActivityId(), activities.get(0).getName()));
        } else {
            activityItem.setSelected(true);
            activityItem.setCount(count);
            teamBuildingPackageItemRepository.save(packageItem);
            return null;
        }
    }

    public void unSelectActivityItem(Long teamBuildingPackageItemId, Long activityItemId) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();
        activityItem.setSelected(false);
        activityItem.setCount(null);

        teamBuildingPackageItemRepository.save(packageItem);

    }

    public Error<MutexActivityErrorDetail> checkMutexActivity(Long teamBuildingPackageItemId, Long activityItemId) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();

        Long mutexActivityId = activityMutexRepository.findByMutexActivityId(packageItem.getPackageId(), activityItem.getActivityId());
        if (mutexActivityId != null) {
            ActivityItem mutexActivity = packageItem.getActivityItems().stream().filter(i -> i.getActivityId() == mutexActivityId).findFirst().get();
            if (mutexActivity.getSelected()) {
                String packageName = teamBuildingPackageRepository.findById(packageItem.getPackageId()).getName();
                Map<Long, String> activityIdToName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), mutexActivityId))
                        .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));

                return new Error<>(ErrorName.MutexActivity.getCode(),
                        ErrorName.MutexActivity.getDescription(),
                        new MutexActivityErrorDetail(teamBuildingPackageItemId, packageItem.getPackageId(), packageName,
                                activityItemId, activityItem.getActivityId(), activityIdToName.get(activityItem.getActivityId()),
                                mutexActivityId, activityIdToName.get(mutexActivityId)));
            }
        }
        return null;
    }


}
