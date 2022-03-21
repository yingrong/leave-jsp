package com.tw.api_maintenance.before;

public class Error {
    private final String message;
    Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
