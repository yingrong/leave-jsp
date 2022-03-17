package com.tw.api_maintenance.after.domain.exception;
import com.tw.api_maintenance.after.domain.error_handling.ErrorDetail;
import com.tw.api_maintenance.after.domain.error_handling.DomainErrorName;

public class SelectActivityException extends Throwable {
    private DomainErrorName domainErrorName;
    private ErrorDetail errorDetail;

    public SelectActivityException(DomainErrorName domainErrorName, ErrorDetail errorDetail) {

        this.domainErrorName = domainErrorName;
        this.errorDetail = errorDetail;
    }

    public DomainErrorName getErrorName() {
        return domainErrorName;
    }

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }
}
