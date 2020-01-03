package com.pawban.communicator.repository;

import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.type.ChatRoomStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Transactional
@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, UUID> {

    @Query("select distinct cr from ChatRoom cr join cr.members u " +
            "where cr.status = :status or u.sessionId = :currentSessionId " +
            "order by cr.name asc")
    List<ChatRoom> findAllByStatusOrMembershipOrderByNameAsc(ChatRoomStatus status, UUID currentSessionId);

    Set<ChatRoom> findAllByMembersContaining(CommunicatorUser user);

    boolean existsByIdAndMembersContaining(UUID chatRoomId, CommunicatorUser member);

    Set<ChatRoom> findAllByOwnerId(UUID ownerId);

    Set<ChatRoom> findAll();

    void deleteAllByOwnerId(UUID ownerId);

}
