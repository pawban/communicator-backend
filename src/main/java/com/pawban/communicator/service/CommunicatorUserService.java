package com.pawban.communicator.service;

import com.pawban.communicator.client.CountriesClient;
import com.pawban.communicator.config.CoreConfiguration;
import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.exception.InvalidUsernameException;
import com.pawban.communicator.exception.NotFoundException;
import com.pawban.communicator.exception.UnavailableUsernameException;
import com.pawban.communicator.repository.ChatRoomRepository;
import com.pawban.communicator.repository.CommunicatorUserRepository;
import com.pawban.communicator.type.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CommunicatorUserService {

    private final CommunicatorUserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CoreConfiguration configuration;
    private final CountriesClient countriesClient;

    @Autowired
    public CommunicatorUserService(final CommunicatorUserRepository userRepository,
                                   final ChatRoomRepository chatRoomRepository,
                                   final CoreConfiguration configuration,
                                   final CountriesClient countriesClient) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.configuration = configuration;
        this.countriesClient = countriesClient;
    }

    public List<CommunicatorUser> getVisibleUsersWithCurrentUser(final UUID currentSessionId) {
        return userRepository.findAllByStatusAndVisibleAndValidUntilAfterOrSessionIdOrderByUsername(
                UserStatus.ACTIVE,
                true,
                LocalDateTime.now(),
                currentSessionId
        );
    }

    public CommunicatorUser getUser(final UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id '" + userId + "' doesn't exist.")
        );
    }

    public CommunicatorUser createUser(final CommunicatorUser user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("Can't create user with id set.");
        }
        if (!isUsernameValid(user.getUsername())) {
            throw new InvalidUsernameException();
        }
        if (!isUsernameAvailable(user.getUsername())) {
            throw new UnavailableUsernameException("Username '" + user.getUsername() + "' is unavailable.");
        }
        if (!countriesClient.countryExists(user.getCountryCode())) {
            throw new NotFoundException("Country with code '" + user.getCountryCode() + "' doesn't exist.");
        }
        user.setSessionId(UUID.randomUUID());
        return userRepository.save(user);
    }

    private boolean isUsernameValid(final String username) {
        return username.matches("\\w{3,30}");
    }

    public Boolean isUsernameAvailable(final String username) {
        return !userRepository.existsByUsernameAndStatusNot(username, UserStatus.DELETED)
                && !configuration.getRestrictedUserNames().contains(username);
    }

    public CommunicatorUser updateUserVisibility(final CommunicatorUser user,
                                                 final boolean visible) {
        user.setVisible(visible);
        return userRepository.save(user);
    }

    public CommunicatorUser getChuckNorris() {
        return userRepository.findByUsername("chuck_norris").orElseGet(() -> {
                    CommunicatorUser chuckNorris = CommunicatorUser.builder()
                            .username("chuck_norris")
                            .status(UserStatus.ACTIVE)
                            .visible(false)
                            .countryCode("USA")
                            .build();
                    return userRepository.save(chuckNorris);
                }
        );
    }

    public int deactivateExpiredUsers() {
        Set<CommunicatorUser> expiredUsers = userRepository.findAllByStatusAndValidUntilBefore(
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );
        expiredUsers.forEach(this::deleteUser);
        userRepository.saveAll(expiredUsers);
        return expiredUsers.size();
    }

    public void deleteUser(final CommunicatorUser user) {
        chatRoomRepository.deleteAllByOwnerId(user.getId());
        Set<ChatRoom> chatRooms = chatRoomRepository.findAllByMembersContaining(user);
        chatRooms.forEach(chatRoom -> chatRoom.getMembers().removeIf(u -> u.getId().equals(user.getId())));
        chatRoomRepository.saveAll(chatRooms);
        user.setStatus(UserStatus.DELETED);
        user.setValidUntil(LocalDateTime.now());
        userRepository.save(user);
    }

    public Set<String> getCountryCodesInUse() {
        return userRepository.findAllByStatusAndValidUntilAfter(UserStatus.ACTIVE, LocalDateTime.now()).stream()
                .map(CommunicatorUser::getCountryCode)
                .collect(Collectors.toSet());
    }

}
