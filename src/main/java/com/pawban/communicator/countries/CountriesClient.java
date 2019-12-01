package com.pawban.communicator.countries;

import com.pawban.communicator.dto.CountryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CountriesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountriesClient.class);

    private final String address = "https://restcountries.eu/rest/v2";
    private final String fields = "name;alpha3Code;flag";

    private final RestTemplate restTemplate;

    @Autowired
    public CountriesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CountryDto> getAllCountries() {
        String endpoint = address + "/all";
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("fields", fields)
                .build(true)
                .toUri();
        try {
            CountryDto[] countriesResponse = restTemplate.getForObject(url, CountryDto[].class);
            return Arrays.asList(Optional.ofNullable(countriesResponse).orElse(new CountryDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
