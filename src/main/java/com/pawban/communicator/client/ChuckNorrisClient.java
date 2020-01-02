package com.pawban.communicator.client;

import com.pawban.communicator.dto.JokeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
public class ChuckNorrisClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountriesClient.class);

    private final String address = "http://api.icndb.com";

    private final RestTemplate restTemplate;

    @Autowired
    public ChuckNorrisClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<JokeDto> getRandomJoke() {
        String endpoint = address + "/jokes/random";
        URI url = UriComponentsBuilder.fromHttpUrl(endpoint)
                .build(true)
                .toUri();
        try {
            JokeDto jokeResponse = restTemplate.getForObject(url, JokeDto.class);
            return Optional.ofNullable(jokeResponse);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
