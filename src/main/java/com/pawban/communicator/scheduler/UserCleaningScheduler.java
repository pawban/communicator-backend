package com.pawban.communicator.scheduler;

import com.pawban.communicator.service.CommunicatorUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserCleaningScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCleaningScheduler.class);

    private final CommunicatorUserService userService;

    @Autowired
    public UserCleaningScheduler(final CommunicatorUserService userService) {
        this.userService = userService;
    }

    @Scheduled(
            fixedRate = 600000L,
            initialDelay = 600000L
    )
    public void userCleaning() {
        LOGGER.info("Cleaning expired users has started.");
        int deletedUsersCount = userService.deactivateExpiredUsers();
        LOGGER.info("Cleaning expired users has ended. " + deletedUsersCount + " user(s) has been deleted.");
    }

}
