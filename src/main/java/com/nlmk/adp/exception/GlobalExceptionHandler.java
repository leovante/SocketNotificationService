package com.nlmk.adp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final ThreadLocal<String> STACK_TRACE = new ThreadLocal<>();

    public static String getStackTrace() {
        return STACK_TRACE.get();
    }

}
