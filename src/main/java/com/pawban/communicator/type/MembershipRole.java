package com.pawban.communicator.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MembershipRole {
    OWNER,
    MEMBER,
    OUTSIDER;

    @JsonValue
    public String getValue() {
        return toString().toLowerCase();
    }

}
