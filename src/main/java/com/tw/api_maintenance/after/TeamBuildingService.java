package com.tw.api_maintenance.after;

import java.util.*;
import java.util.stream.Collectors;

public class TeamBuildingService {
    TeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    TeamBuildingPackageRepository teamBuildingPackageRepository;
    ActivityRepository activityRepository;
    private ActivityMutexRepository activityMutexRepository;
    private ActivityDependentRepository activityDependentRepository;

    public TeamBuildingService(TeamBuildingPackageItemRepository teamBuildingPackageItemRepository, TeamBuildingPackageRepository teamBuildingPackageRepository, ActivityRepository activityRepository, ActivityMutexRepository activityMutexRepository, ActivityDependentRepository activityDependentRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
        this.activityMutexRepository = activityMutexRepository;
        this.activityDependentRepository = activityDependentRepository;
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

        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getId(), activityItemId)).findFirst().get();

        Error<? extends ErrorDetail> error = validateCount(requestCount);
        if (error != null) return error;

        error = validateReliedActivitySelect(packageItem, activityItem);
        if (error != null)
            return error;

        error = validateAlreadySelectedLastTime(packageItem, activityItem);
        if (error != null) return error;

        activityItem.setSelected(true);
        activityItem.setCount(Integer.parseInt(requestCount));
        teamBuildingPackageItemRepository.save(packageItem);
        return null;

    }

    private Error<AlreadySelectedLastTimeErrorDetail> validateAlreadySelectedLastTime(TeamBuildingPackageItem packageItem, ActivityItem activityItem) {
        TeamBuildingPackageItem lastPackageItem = teamBuildingPackageItemRepository.findLastCompleted();

        if (lastPackageItem != null && Objects.equals(lastPackageItem.getPackageId(), packageItem.getPackageId()) &&
                lastPackageItem.getActivityItems().stream()
                        .filter(ai -> ai.getSelected()).anyMatch(ai -> Objects.equals(ai.getActivityId(), activityItem.getActivityId()))) {
            List<Activity> activities = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId()));
            return new Error<>(ErrorName.AlreadySelectedLastTime.getCode(),
                    ErrorName.AlreadySelectedLastTime.getDescription(),
                    new AlreadySelectedLastTimeErrorDetail(activityItem.getId(), activityItem.getActivityId(), activities.get(0).getName()));
        }
        return null;
    }

    private Error<ReliedNotSelectedErrorDetail> validateReliedActivitySelect(TeamBuildingPackageItem packageItem, ActivityItem activityItem) {
        Long reliedId = activityDependentRepository.findByReliedId(packageItem.getPackageId(), activityItem.getActivityId());

        if (reliedId != null) {
            ActivityItem reliedActivityItem = packageItem.getActivityItems().stream().filter(a -> Objects.equals(a.getActivityId(), reliedId)).findFirst().get();
            if (!reliedActivityItem.getSelected()) {
                TeamBuildingPackage packageEntity = teamBuildingPackageRepository.findById(packageItem.getPackageId());
                Map<Long, String> idToActivityName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), reliedId))
                        .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));

                return new Error<>(ErrorName.ReliedNotSelected.getCode(), ErrorName.ReliedNotSelected.getDescription(),
                        new ReliedNotSelectedErrorDetail(packageItem.getId(), packageItem.getPackageId(), packageEntity.getName(),
                                activityItem.getId(), activityItem.getActivityId(), idToActivityName.get(activityItem.getActivityId()),
                                reliedId, idToActivityName.get(reliedId)));
            }
        }
        return null;
    }

    private Error<? extends ErrorDetail> validateCount(String requestCount) {
        Error<? extends ErrorDetail> error = null;

        try {
            Integer count = Integer.parseInt(requestCount);
            if (count < 1 || count > 50) {
                error = new Error<>(ErrorName.NotInRange.getCode(), ErrorName.NotInRange.getDescription(),
                        new NotInRangeErrorDetail(requestCount, 1, 50));
            }
        } catch (NumberFormatException ex) {
            error = new Error<>(ErrorName.UnexpectedType.getCode(), ErrorName.UnexpectedType.getDescription(),
                    new UnexpectedTypeErrorDetail(requestCount, Integer.class.getName()));
        }

        if (error != null) {
            return error;
        }
        return null;
    }

    public void unSelectActivityItem(Long teamBuildingPackageItemId, Long activityItemId) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getId(), activityItemId)).findFirst().get();
        activityItem.setSelected(false);
        activityItem.setCount(null);

        teamBuildingPackageItemRepository.save(packageItem);

    }

    public Error<? extends ErrorDetail> checkMutexActivity(Long teamBuildingPackageItemId, Long activityItemId) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getId(), activityItemId)).findFirst().get();
        return validateMutexActivity(packageItem, activityItem);
    }

    private Error<? extends ErrorDetail> validateMutexActivity(TeamBuildingPackageItem packageItem, ActivityItem activityItem) {
        Long mutexActivityId = activityMutexRepository.findByMutexActivityId(packageItem.getPackageId(), activityItem.getActivityId());
        if (mutexActivityId != null) {
            ActivityItem mutexActivity = packageItem.getActivityItems().stream().filter(i -> Objects.equals(i.getActivityId(), mutexActivityId)).findFirst().get();
            if (mutexActivity.getSelected()) {
                String packageName = teamBuildingPackageRepository.findById(packageItem.getPackageId()).getName();
                Map<Long, String> activityIdToName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), mutexActivityId))
                        .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));

                return new Error<>(ErrorName.MutexActivity.getCode(),
                        ErrorName.MutexActivity.getDescription(),
                        new MutexActivityErrorDetail(packageItem.getId(), packageItem.getPackageId(), packageName,
                                activityItem.getId(), activityItem.getActivityId(), activityIdToName.get(activityItem.getActivityId()),
                                mutexActivityId, activityIdToName.get(mutexActivityId)));
            }
        }
        return null;
    }


}
