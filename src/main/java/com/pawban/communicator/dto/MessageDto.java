package com.pawban.communicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class MessageDto {

    private UUID id;
    private String sender;
    private String text;
    private LocalDateTime creationTime;
    private UUID chatRoomId;

}
