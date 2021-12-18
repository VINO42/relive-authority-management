package com.relive.api;

import lombok.Data;

/**
 * @Author ReLive
 * @Date 2020/7/11-10:38
 * <p>
 * 统一返回结果
 */
@Data
public class Result<T> {
    private Boolean success;
    private Integer code;
    private String message;
    private T data;


    //私有化构造方法
    private Result() {
    }

    private Result(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 返回结果数据
     * @param <T>
     * @return
     */
    public static <T> Result success(T data) {
        return new Result(true, ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    返回结果数据
     * @param message 返回结果信息
     * @param <T>
     * @return
     */
    public static <T> Result success(T data, String message) {
        return new Result(true, ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @return
     */
    public static <T> Result error() {
        return new Result<T>(false, ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param message 返回结构信息
     * @return
     */
    public static <T> Result error(String message) {
        return new Result(false, ResultCode.FAILED.getCode(),
                message, null);
    }

    public static <T> Result error(Integer code,String message) {
        return new Result(false, code,
                message, null);
    }
}
