package com.pawban.communicator.dto;

import com.pawban.communicator.type.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberDto {

    private CommunicatorUserDto user;
    private MembershipRole role;

}
