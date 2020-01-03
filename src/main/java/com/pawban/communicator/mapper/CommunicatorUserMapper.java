package com.pawban.communicator.mapper;

import com.pawban.communicator.client.CountriesClient;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.dto.CommunicatorUserDto;
import com.pawban.communicator.dto.CountryDto;
import com.pawban.communicator.dto.MemberDto;
import com.pawban.communicator.dto.SessionDto;
import com.pawban.communicator.exception.NotFoundException;
import com.pawban.communicator.type.MembershipRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CommunicatorUserMapper {

    private final CountriesClient countriesClient;

    @Autowired
    public CommunicatorUserMapper(final CountriesClient countriesClient) {
        this.countriesClient = countriesClient;
    }

    public CommunicatorUser mapToUser(final CommunicatorUserDto userDto) {
        return CommunicatorUser.builder()
                .username(userDto.getUsername())
                .countryCode(userDto.getCountry().getCountryCode())
                .visible(userDto.isVisible())
                .build();
    }

    public List<CommunicatorUserDto> mapToUserDtoList(final Collection<CommunicatorUser> users) {
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public CommunicatorUserDto mapToUserDto(final CommunicatorUser user) {
        CountryDto countryDto = countriesClient.getCountry(user.getCountryCode()).orElseThrow(
                () -> new NotFoundException("Country with code '" + user.getCountryCode() + "' hasn't been found.")
        );
        return CommunicatorUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .country(countryDto)
                .visible(user.isVisible())
                .build();
    }

    public SessionDto mapToSessionDto(final CommunicatorUser user) {
        return SessionDto.builder()
                .userDto(mapToUserDto(user))
                .sessionId(user.getSessionId())
                .build();
    }

    public List<MemberDto> mapToMembersDtoList(final Collection<CommunicatorUser> users,
                                               final Map<UUID, MembershipRole> membershipRoleMap) {
        return users.stream()
                .map(user -> MemberDto.builder()
                        .user(mapToUserDto(user))
                        .role(membershipRoleMap.getOrDefault(user.getId(), MembershipRole.OUTSIDER))
                        .build()
                )
                .collect(Collectors.toList());
    }

}
