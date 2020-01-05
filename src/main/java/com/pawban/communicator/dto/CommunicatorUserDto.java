package com.pawban.communicator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CommunicatorUserDto {

    private UUID id;
    private String username;
    private CountryDto country;
    private boolean visible;

}
