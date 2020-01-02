package com.pawban.communicator.repository;

import com.pawban.communicator.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {

    List<Message> findAllByChatRoomId(UUID chatRoomId);

}
