package com.tw.api_maintenance.after;

import java.util.*;

public class TeamBuildingPackageItemRepository {
    Map<Long, TeamBuildingPackageItem> packageItemData = new HashMap<>();

    public TeamBuildingPackageItemRepository() {
        packageItemData.put(80010L, new TeamBuildingPackageItem(80010L, 10010L, Arrays.asList(
                new ActivityItem(801L, 1L, true, 10),
                new ActivityItem(802L, 2L, false, null),
                new ActivityItem(803L, 3L, false, null),
                new ActivityItem(804L, 4L, false, null),
                new ActivityItem(805L, 5L, false, null)), new Date(2021, 10, 5), false));

        packageItemData.put(80011L, new TeamBuildingPackageItem(80011L, 10010L, Arrays.asList(
                new ActivityItem(806L, 3L, true, null),
                new ActivityItem(807L, 4L, false, null),
                new ActivityItem(808L, 5L, false, null)), new Date(2021, 9, 1), true));

        packageItemData.put(80086L, new TeamBuildingPackageItem(80086L, 10086L, Arrays.asList(
                new ActivityItem(901L, 1L, false, null),
                new ActivityItem(911L, 11L, false, null),
                new ActivityItem(904L, 4L, false, null)), new Date(2021, 9, 7), true));

    }

    public TeamBuildingPackageItem findById(Long id) {
        return packageItemData.get(id);
    }

    public void save(TeamBuildingPackageItem packageItem) {
        packageItemData.put(packageItem.getId(), packageItem);
    }

    public TeamBuildingPackageItem findLastCompleted() {
        Comparator<TeamBuildingPackageItem> dateDescendComparator = (item1, item2) -> {
            if (item1.getDate().before(item2.getDate())) {
                return -1;
            } else if (item1.getDate().after(item2.getDate())) {
                return 1;
            }
            return 0;
        };
        return packageItemData.values().stream().filter(p -> p.isCompleted()).sorted(dateDescendComparator).findFirst().get();
    }
}
