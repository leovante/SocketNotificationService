package com.nlmk.adp.controllers.error;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nlmk.adp.controllers.model.response.ErrorMessage;

/**
 * AOP Advice для обработки ошибок.
 */
@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    /**
     * Обработка ошибки {@link Exception}.
     *
     * @param exception
     *         Ошибка.
     *
     * @return Сообщение об ошибке.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorMessage handleException(final Exception exception, final HttpServletRequest request) {
        return handleDefault(exception, request);
    }

    /**
     * Обработка ошибки {@link Exception}.
     *
     * @param exception
     *         Ошибка.
     *
     * @return Сообщение об ошибке.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorMessage handleBadRequestsExceptions(final Exception exception, final HttpServletRequest request) {
        return handleDefault(exception, request);
    }

    /**
     * Обработка ошибки {@link Exception}.
     *
     * @param exception
     *         Ошибка.
     *
     * @return Сообщение об ошибке.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage handleConstraintViolationException(final Exception exception,
                                                           final HttpServletRequest request) {
        return handleDefault(exception, request);
    }

    private ErrorMessage handleDefault(Exception exception, HttpServletRequest request) {
        log.error(
                String.format("Failed to process %s request %s", request.getMethod(), request.getRequestURI()),
                exception
        );
        log.error(exception.getMessage(), exception);
        return new ErrorMessage(exception.getMessage());
    }

    /**
     * Обработка ошибки {@link MethodArgumentNotValidException}.
     *
     * @param e
     *         Ошибка.
     *
     * @return Map с описание ошибок полей.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return collectErrors(e.getBindingResult());
    }

    private Map<String, String> collectErrors(final BindingResult bindingResult) {
        final Map<String, String> errors = new HashMap<>();

        bindingResult.getAllErrors()
                     .forEach(error -> {
                         String errorMessage = error.getDefaultMessage();
                         if (error instanceof FieldError) {
                             String fieldName = ((FieldError) error).getField();
                             errors.put(fieldName, errorMessage);
                         } else {
                             errors.put(error.getObjectName(), errorMessage);
                         }
                     });

        return errors;
    }

}
