package com.pawban.communicator.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChatRoomStatus {
    PRIVATE,
    PUBLIC;

    @JsonValue
    public String getValue() {
        return toString().toLowerCase();
    }
}
