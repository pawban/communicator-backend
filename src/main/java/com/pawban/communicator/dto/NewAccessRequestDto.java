package com.pawban.communicator.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NewAccessRequestDto {

    private UUID chatRoomId;
    private String request;

}
