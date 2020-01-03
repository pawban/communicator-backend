package com.pawban.communicator.service;

import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.exception.IllegalOperationException;
import com.pawban.communicator.exception.NotFoundException;
import com.pawban.communicator.repository.ChatRoomRepository;
import com.pawban.communicator.repository.CommunicatorUserRepository;
import com.pawban.communicator.repository.MessageRepository;
import com.pawban.communicator.type.ChatRoomStatus;
import com.pawban.communicator.type.MembershipRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final CommunicatorUserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatRoomService(final ChatRoomRepository chatRoomRepository,
                           final CommunicatorUserRepository userRepository,
                           final MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public Set<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll();
    }

    public Map<UUID, MembershipRole> createMembershipRolesMap(final UUID chatRoomId) {
        Map<UUID, MembershipRole> membershipRoleMap = new HashMap<>();
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        membershipRoleMap.put(chatRoom.getOwner().getId(), MembershipRole.OWNER);
        chatRoom.getMembers().forEach(
                user -> membershipRoleMap.putIfAbsent(user.getId(), MembershipRole.MEMBER)
        );
        return membershipRoleMap;
    }

    public ChatRoom getChatRoom(final UUID chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new NotFoundException("Chat room with the id '" + chatRoomId + "' hasn't been found.")
        );
    }

    public ChatRoom createChatRoom(final ChatRoom chatRoom,
                                   final CommunicatorUser owner) {
        if (chatRoom.getId() != null) {
            throw new IllegalArgumentException("Can't create chat room with id set.");
        }
        chatRoom.setOwner(owner);
        chatRoom.getMembers().add(owner);
        return chatRoomRepository.save(chatRoom);
    }

    public Set<CommunicatorUser> addMemberToChatRoom(final UUID chatRoomId,
                                                     final UUID currentUserId,
                                                     final CommunicatorUser newMember) {
        if (!newMember.isVisible()) {
            throw new IllegalOperationException("You can't add invisible user to the chat room.");
        }
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (isOwnerOfChatRoom(currentUserId, chatRoom)) {
            chatRoom.getMembers().add(newMember);
            return chatRoomRepository.save(chatRoom).getMembers();
        }
        throw new IllegalOperationException("Only owner can add members to his chat room.");
    }

    public boolean isOwnerOfChatRoom(final UUID userId,
                                     final ChatRoom chatRoom) {
        return chatRoom.getOwner().getId().equals(userId);
    }

    public Set<CommunicatorUser> removeMemberFromChatRoom(final UUID chatRoomId,
                                                          final UUID currentUserId,
                                                          final UUID memberId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (isOwnerOfChatRoom(memberId, chatRoom)) {
            throw new IllegalOperationException("Owner can't remove himself from chat room.");
        }
        if (isOwnerOfChatRoom(currentUserId, chatRoom) || currentUserId.equals(memberId)) {
            chatRoom.getMembers().removeIf(member -> member.getId().equals(memberId));
            return chatRoomRepository.save(chatRoom).getMembers();
        }
        throw new IllegalOperationException("Only owner or member himself can remove him from chat room.");
    }

    public ChatRoom updateChatRoom(final UUID chatRoomId,
                                   final UUID currentUserId,
                                   final ChatRoomStatus newStatus,
                                   final CommunicatorUser newOwner) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (!isOwnerOfChatRoom(currentUserId, chatRoom)) {
            throw new IllegalOperationException("Only owner can pass the ownership of the chat room to another user.");
        }
        if (newStatus != null) {
            chatRoom.setStatus(newStatus);
        }
        if (newOwner != null) {
            chatRoom.setOwner(newOwner);
        }
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAvailableChatRooms(final UUID sessionId) {
        return chatRoomRepository.findAllByStatusOrMembershipOrderByNameAsc(ChatRoomStatus.PUBLIC, sessionId);
    }

    public List<CommunicatorUser> getUsersFromChatRoomWithPotentialMembers(final UUID chatRoomId,
                                                                           final UUID currentUserId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (isMemberOfChatRoom(currentUserId, chatRoom)) {
            return userRepository.findAllByChatRoomIdOrVisibleWithPotentialMembersOrderByUsernameAsc(chatRoomId, true);
        }
        throw new IllegalOperationException("Only members of the chat room can retrieve chat room members list with " +
                "potential (possible to add to members) members.");
    }

    public boolean isMemberOfChatRoom(final UUID userId,
                                      @NotNull final ChatRoom chatRoom) {
        return chatRoom.getMembers().stream()
                .anyMatch(member -> member.getId().equals(userId));
    }

    public List<CommunicatorUser> getUsersFromChatRoom(final UUID chatRoomId,
                                                       final UUID currentUserId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if (isMemberOfChatRoom(currentUserId, chatRoom)) {
            return userRepository.findByChatRoomIdOrderByUsernameAsc(chatRoomId);
        }
        throw new IllegalOperationException("Only members of the chat room can retrieve chat room members list.");
    }

    public Set<ChatRoom> getChatRoomsWithMember(final CommunicatorUser user) {
        return chatRoomRepository.findAllByMembersContaining(user);
    }

    public void deleteChatRoom(final UUID chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
    }

    public void deleteUserChatRooms(final UUID userId) {
        Set<ChatRoom> userChatRooms = chatRoomRepository.findAllByOwnerId(userId);
        chatRoomRepository.deleteAll(userChatRooms);
    }

}
