package com.tw.api_maintenance.after.domain.error_handling;

public enum ErrorName {
    AlreadySelectedLastTime("AlreadySelectedLastTime", "上次已经赔付过"),
    MutexActivity("MutexActivity", "互斥的活动"),
    UnexpectedType("UnexpectedType", "非期待的类型"),
    NotInRange("NotInRange", "不在规定范围内"),
    ReliedNotSelected("ReliedNotSelected", "依赖的活动没有选中");
    private String code;
    private String description;

    ErrorName(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}