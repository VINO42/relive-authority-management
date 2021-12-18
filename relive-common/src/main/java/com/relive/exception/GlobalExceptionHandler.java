package com.relive.exception;

import com.relive.api.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常类
 *
 * @Author ReLive
 * @Date 2020/7/12-17:35
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        log.error(ExceptionUtils.getMessage(e));
        return Result.error("服务出错了");
    }


    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result error(BusinessException e) {
        log.error(ExceptionUtils.getMessage(e));
        return Result.error();
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessRE(AccessDeniedException e) {
        log.error("不允许访问", e);
        return Result.error(403, "用户未拥有该权限");
    }
}
