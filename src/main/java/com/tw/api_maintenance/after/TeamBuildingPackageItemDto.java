package com.tw.api_maintenance.after;

import java.util.List;

public class TeamBuildingPackageItemDto {
    private Long id;
    private Long packageId;
    private String name;
    private List<ActivityItemDto> activityItemDtos;

    public TeamBuildingPackageItemDto(Long id, Long packageId, String name, List<ActivityItemDto> activityItemDtos) {
        this.id = id;
        this.packageId = packageId;
        this.name = name;
        this.activityItemDtos = activityItemDtos;
    }

    public List<ActivityItemDto> getActivityItemDtos() {
        return activityItemDtos;
    }

    public Long getId() {
        return id;
    }

    public Long getPackageId() {
        return packageId;
    }

    public String getName() {
        return name;
    }
}
