package com.pawban.communicator.service;

import com.pawban.communicator.domain.UserStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {

    public List<UserStatus> getPickableStatuses() {
        return Arrays.stream(UserStatus.values())
                .filter(UserStatus::isPickable)
                .collect(Collectors.toList());
    }

}
