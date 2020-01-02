package com.pawban.communicator.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SessionExpiredException extends RestException {

    public SessionExpiredException(UUID sessionId) {
        super("Unknown session ID: " + sessionId);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
