package com.pawban.communicator.dto;

import com.pawban.communicator.type.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class ChatRoomDto {

    private UUID id;
    private String name;
    private ChatRoomStatus status;
    private CommunicatorUserDto owner;

}
