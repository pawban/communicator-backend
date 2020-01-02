package com.pawban.communicator.scheduler;

import com.pawban.communicator.client.CountriesClient;
import com.pawban.communicator.service.CommunicatorUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountriesCleanUpScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountriesCleanUpScheduler.class);

    private final CommunicatorUserService userService;
    private final CountriesClient countriesClient;

    @Autowired
    public CountriesCleanUpScheduler(final CommunicatorUserService userService,
                                     final CountriesClient countriesClient) {
        this.userService = userService;
        this.countriesClient = countriesClient;
    }

    //    @Scheduled(cron = "0 30 0 * * *")
    public void removeUnusedCountries() {
        LOGGER.info("Cleaning unused countries from pool has started.");
        countriesClient.releaseUnusedCountries(userService.getCountryCodesInUse());
        LOGGER.info("Cleaning unused countries from pool has ended.");
    }

}
