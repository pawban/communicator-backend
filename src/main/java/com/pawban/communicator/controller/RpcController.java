package com.pawban.communicator.controller;

import com.pawban.communicator.service.CommunicatorUserService;
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

    private final CommunicatorUserService userService;

    @Autowired
    public RpcController(final CommunicatorUserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/isUsernameAvailable",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isUsernameAvailable(@RequestBody String username) {
        LOGGER.info("POST:/v1/rpc/isUsernameAvailable");
        return userService.isUsernameAvailable(username);
    }

}
