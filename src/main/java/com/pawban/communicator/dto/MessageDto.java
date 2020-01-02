package com.pawban.communicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageDto {

    private UUID id;
    private String sender;
    private String text;
    private LocalDateTime creationTime;
    private UUID chatRoomId;

}
