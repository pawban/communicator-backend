package com.pawban.communicator.mapper;

import com.pawban.communicator.domain.UserStatus;
import com.pawban.communicator.dto.UserStatusDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<UserStatusDto> mapToUserStatusDtoList(final List<UserStatus> userStatuses) {
        return userStatuses.stream()
                .map(this::mapToUserStatusDto)
                .collect(Collectors.toList());
    }

    public UserStatusDto mapToUserStatusDto(final UserStatus userStatus) {
        return new UserStatusDto(userStatus.toString(), userStatus.isVisible());
    }

}
