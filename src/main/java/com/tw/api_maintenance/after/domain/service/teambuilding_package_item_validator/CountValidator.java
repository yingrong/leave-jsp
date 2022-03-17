package com.tw.api_maintenance.after.domain.service.teambuilding_package_item_validator;

import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.ErrorName;
import com.tw.api_maintenance.after.domain.error_handling.NotInRangeErrorDetail;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;

public class CountValidator implements TeamBuildingPackageItemValidator{
    @Override
    public void validate(TeamBuildingPackageItem teamBuildingPackageItem, Long activityItemId, Integer count) throws SelectActivityException {
        if (count < 1 || count > 50) {
            throw new SelectActivityException(ErrorName.NotInRange, new NotInRangeErrorDetail(count, 1, 50));
        }
    }
}
