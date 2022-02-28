package com.tw.api_maintenance.after;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamBuildingService {
    TeamBuildingPackageItemRepository teamBuildingPackageItemRepository;
    TeamBuildingPackageRepository teamBuildingPackageRepository;
    ActivityRepository activityRepository;

    public TeamBuildingService(TeamBuildingPackageItemRepository teamBuildingPackageItemRepository, TeamBuildingPackageRepository teamBuildingPackageRepository, ActivityRepository activityRepository) {
        this.teamBuildingPackageItemRepository = teamBuildingPackageItemRepository;
        this.teamBuildingPackageRepository = teamBuildingPackageRepository;
        this.activityRepository = activityRepository;
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

    public Error selectActivityItem(Long teamBuildingPackageItemId, Long activityItemId, Integer count) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();

        if (packageItem.getPackageId() == 10010 && activityItem.getActivityId() == 3) {
            return new Error("上次已经举办过唱歌活动，本次不可选择！");
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

    public HashMap<String, String> checkMutexActivity(Long teamBuildingPackageItemId, Long activityItemId,  Map<Long, Map<Long, Long>> mutexActivityIdMap) {
        TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
        ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();

        if (mutexActivityIdMap.get(packageItem.getPackageId()) != null) {
            Long mutexActivityId = mutexActivityIdMap.get(packageItem.getPackageId()).get(activityItem.getActivityId());
            if (mutexActivityId != null) {
                ActivityItem mutexActivity = packageItem.getActivityItems().stream().filter(i -> i.getActivityId() == mutexActivityId).findFirst().get();
                if (mutexActivity.getSelected()) {
                    String packageName = teamBuildingPackageRepository.findById(packageItem.getPackageId()).getName();
                    Map<Long, String> activityIdToName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), mutexActivityId))
                            .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));
                    String errorMessage = "在" + packageName + "中，" + activityIdToName.get(activityItem.getActivityId()) + "和" + activityIdToName.get(mutexActivityId) + "不能同时选择！";
                    HashMap<String, String> result = new HashMap<>();
                    result.put("errorMessage", errorMessage);
                    return result;
                }
            }
        }
        return null;
    }


}
