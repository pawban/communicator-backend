package com.pawban.communicator.controller;

import com.pawban.communicator.countries.CountriesClient;
import com.pawban.communicator.dto.CountryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rest/countries")
@CrossOrigin("*")
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    private final CountriesClient countriesClient;

    @Autowired
    public CountryController(CountriesClient countriesClient) {
        this.countriesClient = countriesClient;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CountryDto> getAllCountries() {
        LOGGER.info("GET:/v1/rest/countries -> getAllCountries()");
        return countriesClient.getAllCountries();
    }

}
