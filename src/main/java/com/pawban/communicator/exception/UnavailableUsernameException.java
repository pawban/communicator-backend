package com.pawban.communicator.exception;

import org.springframework.http.HttpStatus;

public class UnavailableUsernameException extends RestException {

    public UnavailableUsernameException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

}
