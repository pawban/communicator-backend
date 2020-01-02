package com.pawban.communicator.repository;

import com.pawban.communicator.domain.AccessRequest;
import com.pawban.communicator.type.AccessRequestStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Transactional
@Repository
public interface AccessRequestRepository extends CrudRepository<AccessRequest, UUID> {

    Set<AccessRequest> findAllByChatRoomOwnerIdAndDeliveredFalseAndStatus(UUID chatRoomOwnerId,
                                                                          AccessRequestStatus status);

    Set<AccessRequest> findAllBySenderIdAndDeliveredFalseAndStatusNot(UUID senderId, AccessRequestStatus status);

    boolean existsBySenderIdAndChatRoomIdAndStatus(UUID senderId, UUID chatRoomId, AccessRequestStatus status);

}
