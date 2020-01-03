package com.pawban.communicator.service;

import com.pawban.communicator.domain.AccessRequest;
import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.exception.IllegalOperationException;
import com.pawban.communicator.exception.NotFoundException;
import com.pawban.communicator.repository.AccessRequestRepository;
import com.pawban.communicator.repository.ChatRoomRepository;
import com.pawban.communicator.type.AccessRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccessRequestService {

    public static final Long EXPIRATION_PERIOD = 10L;

    private final AccessRequestRepository accessRequestRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public AccessRequestService(final AccessRequestRepository accessRequestRepository,
                                final ChatRoomRepository chatRoomRepository) {
        this.accessRequestRepository = accessRequestRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public AccessRequest createAccessRequest(final AccessRequest newAccessRequest) {
        if (accessRequestRepository.existsBySenderIdAndChatRoomIdAndStatus(
                newAccessRequest.getSender().getId(),
                newAccessRequest.getChatRoom().getId(),
                AccessRequestStatus.PENDING
        )) {
            throw new IllegalOperationException("You've got pending request to this chat room.");
        }
        if (chatRoomRepository.existsByIdAndMembersContaining(
                newAccessRequest.getChatRoom().getId(),
                newAccessRequest.getSender()
        )) {
            throw new IllegalOperationException("You're already member of this chat room.");
        }
        return accessRequestRepository.save(newAccessRequest);
    }

    public AccessRequest getAccessRequest(final UUID accessRequestId) {
        return accessRequestRepository.findById(accessRequestId).orElseThrow(
                () -> new NotFoundException("Access request with id '" + accessRequestId + "' doesn't exist.")
        );
    }

    public Set<AccessRequest> getChatRoomOwnerUnprocessedAccessRequests(final UUID chatRoomOwnerId) {
        Set<AccessRequest> accessRequests = accessRequestRepository
                .findAllByChatRoomOwnerIdAndDeliveredFalseAndStatus(chatRoomOwnerId, AccessRequestStatus.PENDING);
        accessRequests.forEach(accessRequest -> accessRequest.setDelivered(true));
        return StreamSupport.stream(accessRequestRepository.saveAll(accessRequests).spliterator(), false)
                .collect(Collectors.toSet());
    }

    public AccessRequest changeAccessRequestStatus(final AccessRequest accessRequest,
                                                   final AccessRequestStatus newStatus) {
        accessRequest.setStatus(newStatus);
        accessRequest.setDelivered(false);
        if (newStatus.equals(AccessRequestStatus.ACCEPTED)) {
            ChatRoom chatRoom = accessRequest.getChatRoom();
            chatRoom.getMembers().add(accessRequest.getSender());
            chatRoomRepository.save(chatRoom);
        }
        return accessRequestRepository.save(accessRequest);
    }

    public Set<AccessRequest> getSenderProcessedAccessRequests(final UUID senderId) {
        Set<AccessRequest> accessRequests = accessRequestRepository
                .findAllBySenderIdAndDeliveredFalseAndStatusNot(senderId, AccessRequestStatus.PENDING);
        accessRequests.forEach(accessRequest -> accessRequest.setDelivered(true));
        return StreamSupport.stream(accessRequestRepository.saveAll(accessRequests).spliterator(), false)
                .collect(Collectors.toSet());
    }

    public boolean isChatRoomOwner(final UUID userId,
                                   final AccessRequest accessRequest) {
        return accessRequest.getChatRoom().getOwner().getId().equals(userId);
    }

    public int rejectExpiredAccessRequests() {
        Set<AccessRequest> accessRequests = accessRequestRepository.findAllByStatusAndCreationTimeIsBefore(
                AccessRequestStatus.PENDING,
                LocalDateTime.now().minusMinutes(EXPIRATION_PERIOD)
        );
        accessRequests.forEach(accessRequest -> {
            accessRequest.setStatus(AccessRequestStatus.REJECTED);
            accessRequest.setDelivered(false);
        });
        accessRequestRepository.saveAll(accessRequests);
        return accessRequests.size();
    }

}
