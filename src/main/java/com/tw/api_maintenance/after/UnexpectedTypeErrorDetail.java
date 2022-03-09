package com.tw.api_maintenance.after;

import java.lang.reflect.Type;

public class UnexpectedTypeErrorDetail extends ErrorDetail {
    private String inputValue;
    private String expectedType;

    UnexpectedTypeErrorDetail(String inputValue, String expectedType) {
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

