package com.tw.api_maintenance.after.application.error_handling;

import com.tw.api_maintenance.after.domain.error_handling.ErrorDetail;

public class Error<T extends ErrorDetail> {
    private String code;
    private String description;
    private T detail;

    public Error(String code, String description, T detail) {
        this.code = code;
        this.description = description;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public T getDetail() {
        return detail;
    }
}

