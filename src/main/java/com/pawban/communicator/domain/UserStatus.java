package com.pawban.communicator.domain;

import lombok.Getter;

@Getter
public enum UserStatus {
    VISIBLE(true, true),
    HIDDEN(false, true),
    DELETED(false, false);

    private boolean visible;
    private boolean pickable;

    UserStatus(boolean visible, boolean pickable) {
        this.visible = visible;
        this.pickable = pickable;
    }

//    public boolean isVisible() {
//        return visible;
//    }
//
//    public boolean isPickable() {
//        return pickable;
//    }

}
