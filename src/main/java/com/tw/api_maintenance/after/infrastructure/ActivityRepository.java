package com.tw.api_maintenance.after.infrastructure;

import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.repository.IActivityRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivityRepository implements IActivityRepository {
    Map<Long, Activity> activityData = new HashMap<>();

    public ActivityRepository() {
        activityData.put(1L, new Activity(1L, "冬奥两日游"));
        activityData.put(2L, new Activity(2L, "户外探险一日游"));
        activityData.put(3L, new Activity(3L, "唱歌"));
        activityData.put(4L, new Activity(4L, "吃饭"));
        activityData.put(5L, new Activity(5L, "住宿"));
        activityData.put(11L, new Activity(11L, "自由三日飞"));
    }

    @Override
    public List<Activity> findByIds(List<Long> ids) {
        return ids.stream().map(id -> activityData.get(id)).collect(Collectors.toList());
    }
}
