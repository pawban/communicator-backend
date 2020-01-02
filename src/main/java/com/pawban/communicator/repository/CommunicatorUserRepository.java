package com.pawban.communicator.repository;

import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.type.UserStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Transactional
@Repository
public interface CommunicatorUserRepository extends CrudRepository<CommunicatorUser, UUID> {

    List<CommunicatorUser> findAllByStatusAndVisibleAndValidUntilAfterOrSessionIdOrderByUsername(
            UserStatus status, boolean visible, LocalDateTime time, UUID sessionId);

    boolean existsBySessionIdAndStatusIsNotAndValidUntilIsAfter(UUID sessionId, UserStatus status, LocalDateTime time);

    Optional<CommunicatorUser> findBySessionId(UUID sessionId);

    boolean existsByUsernameAndStatusNot(String username, UserStatus status);

    @Query("select u from CommunicatorUser u where u.id in " +
            "(select m.id from ChatRoom cr join cr.members m where cr.id = :chatRoomId) " +
            "or u.id in " +
            "(select u1.id from CommunicatorUser u1 where u1.visible = :visible)" +
            "order by u.username asc")
    List<CommunicatorUser> findAllByChatRoomIdOrVisibleWithPotentialMembersOrderByUsername(UUID chatRoomId, boolean visible);

    //    @Query("select u from CommunicatorUser u where u.id in " +
//            "(select m.id from ChatRoom cr join cr.members m where cr.id = :chatRoomId) " +
//            "order by u.username asc")
    @Query("select u from ChatRoom cr join cr.members u " +
            "where cr.id = :chatRoomId " +
            "order by u.username asc")
    List<CommunicatorUser> findByChatRoomIdOrderByUsername(UUID chatRoomId);

    Optional<CommunicatorUser> findByUsername(String username);

    Set<CommunicatorUser> findAllByStatusAndValidUntilBefore(UserStatus status, LocalDateTime dateTime);

    Set<CommunicatorUser> findAllByStatusAndValidUntilAfter(UserStatus status, LocalDateTime dateTime);

}
