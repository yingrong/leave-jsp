package com.tw.api_maintenance.after.domain.error_handling;

public class NotInRangeErrorDetail extends ErrorDetail {
    private String inputValue;
    private Integer min;
    private Integer max;

    public NotInRangeErrorDetail(String inputValue, Integer min, Integer max) {

        this.inputValue = inputValue;
        this.min = min;
        this.max = max;
    }

    public String getInputValue() {
        return inputValue;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
