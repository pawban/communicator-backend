package com.pawban.communicator.exception;

import org.springframework.http.HttpStatus;

public class IllegalOperationException extends RestException {

    public IllegalOperationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }

}
