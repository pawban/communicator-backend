package com.pawban.communicator.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NewMessageDto {

    private String text;
    private UUID chatRoomId;

}
