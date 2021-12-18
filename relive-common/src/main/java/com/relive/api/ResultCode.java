package com.relive.api;

/**
 * @Author ReLive
 * @Date 2020/9/28-22:00
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "成功"),
    FAILED(500, "系统服务异常");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
