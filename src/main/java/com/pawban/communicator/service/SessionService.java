package com.pawban.communicator.service;

import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.exception.NotFoundException;
import com.pawban.communicator.exception.SessionExpiredException;
import com.pawban.communicator.repository.CommunicatorUserRepository;
import com.pawban.communicator.type.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.pawban.communicator.domain.CommunicatorUser.SESSION_VALIDITY_PERIOD;

@Service
public class SessionService {

    public static final String AUTH_HEADER = "X-Auth-Key";

    private final CommunicatorUserRepository userRepository;

    @Autowired
    public SessionService(final CommunicatorUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateSession(final UUID sessionId) {
        if (userRepository.existsBySessionIdAndStatusIsNotAndValidUntilIsAfter(
                sessionId, UserStatus.DELETED, LocalDateTime.now())) {
            extendSessionValidityPeriod(sessionId);
            return;
        }
        throw new SessionExpiredException(sessionId);
    }

    private void extendSessionValidityPeriod(final UUID sessionId) {
        CommunicatorUser user = getCurrentUser(sessionId);
        user.setValidUntil(LocalDateTime.now().plusMinutes(SESSION_VALIDITY_PERIOD));
        userRepository.save(user);
    }

    public CommunicatorUser getCurrentUser(final UUID sessionId) {
        return userRepository.findBySessionId(sessionId).orElseThrow(
                () -> new NotFoundException("Session with id '" + sessionId + "' doesn't exist.")
        );
    }

    public UUID getCurrentUserId(final UUID sessionId) {
        return getCurrentUser(sessionId).getId();
    }

}
