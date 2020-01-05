package com.pawban.communicator.dto;

import com.pawban.communicator.type.AccessRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class AccessRequestDto {

    private UUID id;
    private CommunicatorUserDto sender;
    private ChatRoomDto chatRoom;
    private String request;
    private AccessRequestStatus status;

}
