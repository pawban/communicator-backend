package com.pawban.communicator.repository;

import com.pawban.communicator.domain.RecipientMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface RecipientMessageRepository extends CrudRepository<RecipientMessage, UUID> {

    List<RecipientMessage> findAllByRecipientIdOrderByMessageCreationTimeAsc(UUID recipientId);

    List<RecipientMessage> findAllByDeliveredIsFalseAndRecipientIdOrderByMessageCreationTimeAsc(UUID recipientId);

    List<RecipientMessage> findAllByRecipientIdAndMessageChatRoomIdOrderByMessageCreationTimeAsc(UUID recipientId,
                                                                                                 UUID chatRoomId);

    List<RecipientMessage> findAllByDeliveredIsFalseAndRecipientIdAndMessageChatRoomIdOrderByMessageCreationTimeAsc(
            UUID recipientId,
            UUID chatRoomId
    );

}