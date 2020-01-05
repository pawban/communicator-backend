package com.pawban.communicator.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class NewMessageDto {

    private String text;
    private UUID chatRoomId;

}
