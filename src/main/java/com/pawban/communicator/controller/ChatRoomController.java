package com.pawban.communicator.controller;

import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.dto.ChatRoomDto;
import com.pawban.communicator.dto.MemberDto;
import com.pawban.communicator.dto.MessageDto;
import com.pawban.communicator.mapper.ChatRoomMapper;
import com.pawban.communicator.mapper.CommunicatorUserMapper;
import com.pawban.communicator.mapper.MessageMapper;
import com.pawban.communicator.service.ChatRoomService;
import com.pawban.communicator.service.CommunicatorUserService;
import com.pawban.communicator.service.MessageService;
import com.pawban.communicator.service.SessionService;
import com.pawban.communicator.type.ChatRoomStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.pawban.communicator.service.SessionService.AUTH_HEADER;

@RestController
@RequestMapping("/v1/rest/chatRooms")
@CrossOrigin("*")
public class ChatRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomController.class);

    private final ChatRoomService chatRoomService;
    private final CommunicatorUserService userService;
    private final MessageService messageService;
    private final SessionService sessionService;
    private final ChatRoomMapper chatRoomMapper;
    private final CommunicatorUserMapper userMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public ChatRoomController(final ChatRoomService chatRoomService,
                              final CommunicatorUserService userService,
                              final MessageService messageService,
                              final SessionService sessionService,
                              final ChatRoomMapper chatRoomMapper,
                              final CommunicatorUserMapper userMapper,
                              final MessageMapper messageMapper) {
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.messageService = messageService;
        this.sessionService = sessionService;
        this.chatRoomMapper = chatRoomMapper;
        this.userMapper = userMapper;
        this.messageMapper = messageMapper;
    }

    @GetMapping(
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ChatRoomDto> getChatRooms(@RequestHeader(AUTH_HEADER) UUID sessionId) {
        LOGGER.info("GET:/v1/rest/chatRooms -> getChatRooms()");
        sessionService.validateSession(sessionId);
        return chatRoomMapper.mapToChatRoomDtoList(chatRoomService.getAvailableChatRooms(sessionId));
    }

    @GetMapping(
            path = "/{chatRoomId}/members",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MemberDto> getMembersOfChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                                @PathVariable UUID chatRoomId,
                                                @RequestParam(defaultValue = "true") boolean includePotentialMembers) {
        LOGGER.info("GET/v1/rest/chatRooms/members -> getMembersOfChatRoom()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        List<CommunicatorUser> users;
        if (includePotentialMembers) {
            users = chatRoomService.getUsersFromChatRoomWithPotentialMembers(chatRoomId, currentUserId);
        } else {
            users = chatRoomService.getUsersFromChatRoom(chatRoomId, currentUserId);
        }
        return userMapper.mapToMembersDtoList(
                users,
                chatRoomService.createMembershipRolesMap(chatRoomId)
        );
    }

    @GetMapping(
            path = "/{chatRoomId}/messages",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MessageDto> getMessagesFromChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                                    @RequestParam(defaultValue = "true") boolean undeliveredOnly,
                                                    @PathVariable UUID chatRoomId) {
        LOGGER.info("GET:/v1/rest/chatRooms/messages -> getMessagesFromChatRoom()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        return messageMapper.mapToMessageDtoList(
                messageService.getRecipientMessagesFromChatRoom(currentUserId, undeliveredOnly, chatRoomId)
        );
    }

    @PostMapping(
            headers = AUTH_HEADER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ChatRoomDto createChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                      @RequestBody ChatRoomDto chatRoomDto) {
        LOGGER.info("POST:/v1/rest/chatRooms -> createChatRoom()");
        sessionService.validateSession(sessionId);
        CommunicatorUser owner = sessionService.getCurrentUser(sessionId);
        return chatRoomMapper.mapToChatRoomDto(chatRoomService.createChatRoom(
                chatRoomMapper.mapToChatRoom(chatRoomDto),
                owner
        ));
    }

    @PutMapping(
            path = "/{chatRoomId}",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ChatRoomDto updateChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                      @PathVariable UUID chatRoomId,
                                      @RequestParam(required = false) ChatRoomStatus status,
                                      @RequestParam(required = false) UUID ownerId) {
        LOGGER.info("PUT:/v1/rest/chtRooms -> updateChatRoom()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        CommunicatorUser newOwner = ownerId == null ? null : userService.getUser(ownerId);
        return chatRoomMapper.mapToChatRoomDto(
                chatRoomService.updateChatRoom(chatRoomId, currentUserId, status, newOwner)
        );
    }

    @PutMapping(
            path = "/{chatRoomId}/members/{newMemberId}",
            headers = AUTH_HEADER
    )
    public void addMemberToChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                    @PathVariable UUID chatRoomId,
                                    @PathVariable UUID newMemberId) {
        LOGGER.info("PUT:/v1/rest/chatRooms/members -> addMemberToChatRoom()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        CommunicatorUser newMember = userService.getUser(newMemberId);
        chatRoomService.addMemberToChatRoom(chatRoomId, currentUserId, newMember);
    }

    @DeleteMapping(
            path = "/{chatRoomId}",
            headers = AUTH_HEADER
    )
    public void deleteChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                               @PathVariable UUID chatRoomId) {
        LOGGER.info("DELETE:/v1/rest/chatRooms -> deleteChatRoom()");
        sessionService.validateSession(sessionId);
        chatRoomService.deleteChatRoom(chatRoomId);
    }

    @DeleteMapping(
            path = "/{chatRoomId}/members/{memberId}",
            headers = AUTH_HEADER
    )
    public void removeMemberFromChatRoom(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                         @PathVariable UUID chatRoomId,
                                         @PathVariable UUID memberId) {
        LOGGER.info("DELETE:/v1/rest/chatRooms/members -> removeMemberFromChatRoom()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        chatRoomService.removeMemberFromChatRoom(chatRoomId, currentUserId, memberId);
    }

}
