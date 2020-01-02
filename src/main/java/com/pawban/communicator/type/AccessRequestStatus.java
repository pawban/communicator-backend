package com.pawban.communicator.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccessRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    @JsonValue
    public String getValue() {
        return toString().toLowerCase();
    }

}
