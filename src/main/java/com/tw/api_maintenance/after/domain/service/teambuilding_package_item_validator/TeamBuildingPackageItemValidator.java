package com.tw.api_maintenance.after.domain.service.teambuilding_package_item_validator;

import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;

public interface TeamBuildingPackageItemValidator {
   void validate(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException;
}
