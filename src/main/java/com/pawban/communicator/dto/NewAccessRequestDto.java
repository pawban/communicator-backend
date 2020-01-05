package com.pawban.communicator.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class NewAccessRequestDto {

    private UUID chatRoomId;
    private String request;

}
