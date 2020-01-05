package com.pawban.communicator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class MessageDto {

    private UUID id;
    private String sender;
    private String text;
    private LocalDateTime creationTime;
    private UUID chatRoomId;

}
