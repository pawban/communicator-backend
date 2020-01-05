package com.pawban.communicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class SessionDto {

    private UUID sessionId;
    private CommunicatorUserDto userDto;

}
