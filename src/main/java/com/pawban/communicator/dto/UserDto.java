package com.pawban.communicator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private UUID id;
    private String username;
    private CountryDto country;
    private UserStatusDto status;

}
