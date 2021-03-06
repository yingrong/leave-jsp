package com.tw.api_maintenance.before;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TeamBuildingPackageRepository {
    Map<Long, TeamBuildingPackage> packageData = new HashMap<>();

    public TeamBuildingPackageRepository() {
        packageData.put(10010L, new TeamBuildingPackage(10010L, "十万用户团建礼包", Arrays.asList(1L, 2L, 3L, 4L, 5L)));
        packageData.put(10086L, new TeamBuildingPackage(10086L, "百万用户团建礼包", Arrays.asList(1L, 11L, 4L)));
    }

    public TeamBuildingPackage findById(Long id) {
        return packageData.get(id);
    }
}
