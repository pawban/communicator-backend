package com.pawban.communicator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class SessionDto {

    private UUID sessionId;
    private CommunicatorUserDto userDto;

}
