package com.tw.api_maintenance.before;

import java.util.List;

public class TeamBuildingPackage {
    private Long id;
    private String name;
    private List<Long> activityIds;

    public TeamBuildingPackage(Long id, String name, List<Long> activityIds){
        this.id = id;
        this.name = name;
        this.activityIds = activityIds;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
