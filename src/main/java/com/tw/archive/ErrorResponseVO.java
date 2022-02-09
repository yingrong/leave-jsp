package com.tw.archive;

public class ErrorResponseVO {
    private int code;
    private String msg;

    public ErrorResponseVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
