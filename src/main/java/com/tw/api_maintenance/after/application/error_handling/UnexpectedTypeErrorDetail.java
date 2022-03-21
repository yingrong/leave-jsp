package com.tw.api_maintenance.after.application.error_handling;

import com.tw.api_maintenance.after.domain.error_handling.ErrorDetail;

public class UnexpectedTypeErrorDetail extends ErrorDetail {
    private String inputValue;
    private String expectedType;

    public UnexpectedTypeErrorDetail(String inputValue, String expectedType) {
        this.inputValue = inputValue;
        this.expectedType = expectedType;
    }

    public String getInputValue() {
        return inputValue;
    }

    public String getExpectedType() {
        return expectedType;
    }
}

