package com.pawban.communicator.dto;

import com.pawban.communicator.type.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MemberDto {

    private CommunicatorUserDto user;
    private MembershipRole role;

}
