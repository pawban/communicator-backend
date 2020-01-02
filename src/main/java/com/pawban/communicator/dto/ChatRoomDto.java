package com.pawban.communicator.dto;

import com.pawban.communicator.type.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatRoomDto {

    private UUID id;
    private String name;
    private ChatRoomStatus status;
    private CommunicatorUserDto owner;

}
