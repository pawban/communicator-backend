package com.pawban.communicator.client;

import com.pawban.communicator.dto.CountryDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CountryPool {

    private static CountryPool instance;

    private Map<String, CountryDto> countries;

    private CountryPool() {
        this.countries = new HashMap<>();
    }

    public static CountryPool getInstance() {
        if (instance == null) {
            instance = new CountryPool();
        }
        return instance;
    }

    public CountryDto getCountry(final String countryCode) {
        return countries.get(countryCode);
    }

    public void addCountry(final CountryDto country) {
        countries.put(country.getCountryCode(), country);
    }

    public boolean contains(final String countryCode) {
        return countries.containsKey(countryCode);
    }

    public void releaseUnusedCountries(final Set<String> countryCodesInUse) {
        this.countries = countries.values().stream()
                .filter(country -> countryCodesInUse.contains(country.getCountryCode()))
                .collect(Collectors.toMap(CountryDto::getCountryCode, country -> country));
    }

}
