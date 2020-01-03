package com.pawban.communicator.client;

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
import java.util.Set;

@Component
public class CountriesClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountriesClient.class);

    private final String address = "https://restcountries.eu/rest/v2";
    private final String fields = "name;alpha3Code;flag";

    private final CountryPool countryPool = CountryPool.getInstance();

    private final RestTemplate restTemplate;

    @Autowired
    public CountriesClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CountryDto> getCountries() {
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

    public Optional<CountryDto> getCountry(final String countryCode) {
        if (countryExists(countryCode)) {
            return Optional.of(countryPool.getCountry(countryCode));
        }
        return Optional.empty();
    }

    public boolean countryExists(final String countryCode) {
        return countryPool.contains(countryCode) || existsCountryInExternalSource(countryCode);
    }

    private boolean existsCountryInExternalSource(final String countryCode) {
        String endpoint = address + "/alpha/" + countryCode;
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("fields", fields)
                .build(true)
                .toUri();
        try {
            CountryDto countryResponse = restTemplate.getForObject(url, CountryDto.class);
            if (countryResponse != null) {
                countryPool.addCountry(countryResponse);
                return true;
            }
            return false;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public void releaseUnusedCountries(final Set<String> countryCodesInUse) {
        countryPool.releaseUnusedCountries(countryCodesInUse);
    }

}
