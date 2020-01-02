package com.pawban.communicator.dto;

import com.pawban.communicator.type.AccessRequestStatus;
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
public class AccessRequestDto {

    private UUID id;
    private CommunicatorUserDto sender;
    private ChatRoomDto chatRoom;
    private String request;
    private AccessRequestStatus status;

}
