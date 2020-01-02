package com.pawban.communicator.exception;

import com.pawban.communicator.dto.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(RestException.class)
    protected ResponseEntity<Object> handleRestException(RestException exception, WebRequest request) {
        return buildResponseEntity(exception, request, exception.getHttpStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(RuntimeException exception, WebRequest request,
                                                       HttpStatus status) {
        ExceptionDto bodyOfResponse = ExceptionDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(exception.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        LOGGER.error(bodyOfResponse.toString());
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException exception, WebRequest request) {
        return buildResponseEntity(exception, request, HttpStatus.BAD_REQUEST);
    }

}
