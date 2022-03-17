package com.tw.api_maintenance.after.application.error_handling;
import com.tw.api_maintenance.after.domain.error_handling.ErrorName;

public enum ApplicationErrorName implements ErrorName {
    UnexpectedType("UnexpectedType", "非期待的类型");
    private String code;
    private String description;

    ApplicationErrorName(String code, String description) {
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
