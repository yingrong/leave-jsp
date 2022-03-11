package com.tw.api_maintenance.after.domain.exception;
import com.tw.api_maintenance.after.domain.error_handling.ErrorDetail;
import com.tw.api_maintenance.after.domain.error_handling.ErrorName;

public class SelectActivityException extends Throwable {
    private ErrorName errorName;
    private ErrorDetail errorDetail;

    public SelectActivityException(ErrorName errorName, ErrorDetail errorDetail) {

        this.errorName = errorName;
        this.errorDetail = errorDetail;
    }

    public ErrorName getErrorName() {
        return errorName;
    }

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }
}
