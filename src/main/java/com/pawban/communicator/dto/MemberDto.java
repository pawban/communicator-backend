package com.pawban.communicator.dto;

import com.pawban.communicator.type.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemberDto {

    private CommunicatorUserDto user;
    private MembershipRole role;

}
