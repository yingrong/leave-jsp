package com.tw.api_maintenance.after.domain.repository;

import com.tw.api_maintenance.after.domain.entity.Activity;

import java.util.List;

public interface IActivityRepository {
    List<Activity> findByIds(List<Long> ids);
}
