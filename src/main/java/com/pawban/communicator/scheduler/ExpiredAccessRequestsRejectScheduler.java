package com.pawban.communicator.scheduler;

import com.pawban.communicator.service.AccessRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiredAccessRequestsRejectScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpiredAccessRequestsRejectScheduler.class);

    private final AccessRequestService accessRequestService;

    @Autowired
    public ExpiredAccessRequestsRejectScheduler(final AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @Scheduled(
            fixedRate = 60000L,
            initialDelay = 60000L
    )
    public void rejectExpiredAccessRequest() {
        LOGGER.info("Rejecting expired access requests has started.");
        int rejectedAccessRequestsCount = accessRequestService.rejectExpiredAccessRequests();
        LOGGER.info("Rejecting expired access requests has ended. " + rejectedAccessRequestsCount +
                " access request(s) has been rejected.");
    }

}
