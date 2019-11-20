package com.pawban.communicator.domain;

public enum UserStatus {
    VISIBLE(true),
    HIDDEN(false),
    DELETED(false);

    private boolean visible;

    UserStatus(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

}
