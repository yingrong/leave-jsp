package com.tw.api_maintenance.after;

public enum ErrorName {
    AlreadySelectedLastTime("AlreadySelectedLastTime", "上次已经赔付过"),
    MutexActivity("MutexActivity", "互斥的活动"),
    UnexpectedType("UnexpectedType", "非期待的类型"),
    NotInRange("NotInRange", "不在规定范围内");

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
