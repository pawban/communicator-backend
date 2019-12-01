package com.pawban.communicator.controller;

import com.pawban.communicator.dto.SessionDto;
import com.pawban.communicator.dto.UserDto;
import com.pawban.communicator.dto.UserStatusDto;
import com.pawban.communicator.mapper.UserMapper;
import com.pawban.communicator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/rest/users")
@CrossOrigin("*")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/statuses", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserStatusDto> getUserStatuses() {
        LOGGER.info("GET:/v1/rest/users/statuses -> getUserStatuses()");
        return userMapper.mapToUserStatusDtoList(userService.getPickableStatuses());
    }

    @GetMapping(headers = "x-auth-key")
    public List<SessionDto> getAllUsers(@RequestHeader("x-auth-key") String authKey) {
        System.out.println("Header x-auth-kay: " + authKey);
        return Collections.emptyList();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionDto createUser(@RequestBody UserDto userDto) {
        LOGGER.info("POST:/v1/rest/users/statuses -> createUser()");
        userDto.setId(UUID.randomUUID());
        return new SessionDto(UUID.randomUUID(), userDto);
    }

}
