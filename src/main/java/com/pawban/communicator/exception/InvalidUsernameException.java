package com.pawban.communicator.exception;

public class InvalidUsernameException extends RuntimeException {

    public InvalidUsernameException() {
        super("Username contains not allowed characters.");
    }

}
