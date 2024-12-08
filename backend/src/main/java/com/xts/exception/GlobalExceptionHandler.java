package com.xts.exception;

import com.xts.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class) //捕获所有异常
    public Result ex(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.error("操作失败！");
    }
}
