package com.pawban.communicator.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
public class CountryDto {

    private String name;

    @JsonAlias("alpha3Code")
    private String countryCode;

    @JsonAlias("flag")
    private String flagUrl;

}
