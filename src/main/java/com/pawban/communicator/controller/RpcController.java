package com.pawban.communicator.controller;

import com.pawban.communicator.dto.UsernameValidationDto;
import com.pawban.communicator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rpc")
public class RpcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcController.class);

    private final UserService userService;

    @Autowired
    public RpcController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/isUsernameAvailable", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsernameValidationDto isUsernameAvailable(@RequestBody UsernameValidationDto usernameValidationDto) {
        LOGGER.info("POST:/v1/rpc/isUsernameAvailable");
        if (usernameValidationDto.getUsername().equals("gawelx")) {
            usernameValidationDto.setAvailable(false);
        } else {
            usernameValidationDto.setAvailable(true);
        }
        return usernameValidationDto;
    }

}
