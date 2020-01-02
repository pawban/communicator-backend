package com.pawban.communicator.controller;

import com.pawban.communicator.domain.AccessRequest;
import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.dto.AccessRequestDto;
import com.pawban.communicator.dto.ChatRoomDto;
import com.pawban.communicator.dto.CommunicatorUserDto;
import com.pawban.communicator.dto.NewAccessRequestDto;
import com.pawban.communicator.dto.SessionDto;
import com.pawban.communicator.exception.IllegalOperationException;
import com.pawban.communicator.mapper.AccessRequestMapper;
import com.pawban.communicator.mapper.ChatRoomMapper;
import com.pawban.communicator.mapper.CommunicatorUserMapper;
import com.pawban.communicator.service.AccessRequestService;
import com.pawban.communicator.service.ChatRoomService;
import com.pawban.communicator.service.CommunicatorUserService;
import com.pawban.communicator.service.SessionService;
import com.pawban.communicator.type.AccessRequestRole;
import com.pawban.communicator.type.AccessRequestStatus;
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
import java.util.Set;
import java.util.UUID;

import static com.pawban.communicator.service.SessionService.AUTH_HEADER;

@RestController
@RequestMapping("/v1/rest/users")
@CrossOrigin("*")
public class CommunicatorUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicatorUserController.class);

    private final SessionService sessionService;
    private final CommunicatorUserService userService;
    private final ChatRoomService chatRoomService;
    private final AccessRequestService accessRequestService;
    private final CommunicatorUserMapper userMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final AccessRequestMapper accessRequestMapper;

    @Autowired
    public CommunicatorUserController(final SessionService sessionService,
                                      final CommunicatorUserService userService,
                                      final ChatRoomService chatRoomService,
                                      final AccessRequestService accessRequestService,
                                      final CommunicatorUserMapper userMapper,
                                      final ChatRoomMapper chatRoomMapper,
                                      final AccessRequestMapper accessRequestMapper) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.accessRequestService = accessRequestService;
        this.userMapper = userMapper;
        this.chatRoomMapper = chatRoomMapper;
        this.accessRequestMapper = accessRequestMapper;
    }

    @GetMapping(
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<CommunicatorUserDto> getActiveUsers(@RequestHeader(AUTH_HEADER) UUID sessionId) {
        LOGGER.info("GET:/v1/rest/users -> getActiveUsers()");
        sessionService.validateSession(sessionId);
        return userMapper.mapToUserDtoList(userService.getVisibleUsersWithCurrentUser(sessionId));
    }

    @GetMapping(
            path = "/sessions",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SessionDto getSession(@RequestHeader(AUTH_HEADER) UUID sessionId) {
        LOGGER.info("GET:/v1/rest/users/sessions -> getSession()");
        sessionService.validateSession(sessionId);
        CommunicatorUser currentUser = sessionService.getCurrentUser(sessionId);
        return userMapper.mapToSessionDto(currentUser);
    }

    @GetMapping(
            path = "/sessions/chatRooms",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ChatRoomDto> getCurrentUserChatRooms(@RequestHeader(AUTH_HEADER) UUID sessionId) {
        LOGGER.info("GET:/v1/rest/sessions/chatRooms -> getCurrentUserChatRooms()");
        sessionService.validateSession(sessionId);
        CommunicatorUser currentUser = sessionService.getCurrentUser(sessionId);
        return chatRoomMapper.mapToChatRoomDtoList(chatRoomService.getChatRoomsWithMember(currentUser));
    }

    @GetMapping(
            path = "/accessRequests",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Set<AccessRequestDto> getUndeliveredAccessRequests(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                                              @RequestParam(defaultValue = "sender") AccessRequestRole role) {
        LOGGER.info("GET:/v1/rest/accessRequests -> getUndeliveredAccessRequests()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        Set<AccessRequest> accessRequests;
        if (role.equals(AccessRequestRole.SENDER)) {
            accessRequests = accessRequestService.getSenderProcessedAccessRequests(currentUserId);
        } else {
            accessRequests = accessRequestService.getChatRoomOwnerUnprocessedAccessRequests(currentUserId);
        }
        return accessRequestMapper.mapToAccessRequestDtoSet(accessRequests);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SessionDto createUser(@RequestBody CommunicatorUserDto userDto) {
        LOGGER.info("POST:/v1/rest/users -> createUser()");
        return userMapper.mapToSessionDto(userService.createUser(userMapper.mapToUser(userDto)));
    }

    @PostMapping(
            path = "/accessRequests",
            headers = AUTH_HEADER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AccessRequestDto createAccessRequest(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                                @RequestBody NewAccessRequestDto newAccessRequestDto) {
        LOGGER.info("POST:/v1/rest/accessRequests -> createAccessRequest()");
        sessionService.validateSession(sessionId);
        CommunicatorUser currentUser = sessionService.getCurrentUser(sessionId);
        ChatRoom chatRoom = chatRoomService.getChatRoom(newAccessRequestDto.getChatRoomId());
        AccessRequest newAccessRequest = accessRequestMapper.mapToAccessRequest(
                newAccessRequestDto,
                currentUser,
                chatRoom
        );
        return accessRequestMapper.mapToAccessRequestDto(accessRequestService.createAccessRequest(newAccessRequest));
    }

    @PutMapping(
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommunicatorUserDto updateUserVisibility(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                                    @RequestParam boolean visible) {
        LOGGER.info("PUT:/v1/rest/users -> updateUserVisibility()");
        sessionService.validateSession(sessionId);
        CommunicatorUser currentUser = sessionService.getCurrentUser(sessionId);
        return userMapper.mapToUserDto(userService.updateUserVisibility(currentUser, visible));
    }

    @PutMapping(
            path = "/accessRequests/{accessRequestId}",
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AccessRequestDto changeAccessRequestStatus(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                                      @PathVariable UUID accessRequestId,
                                                      @RequestParam AccessRequestStatus status) {
        LOGGER.info("PUT:/v1/rest/users/accessRequests -> changeAccessRequestStatus()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        AccessRequest accessRequest = accessRequestService.getAccessRequest(accessRequestId);
        if (!accessRequestService.isChatRoomOwner(currentUserId, accessRequest)) {
            throw new IllegalOperationException("Only owner of chat room can change status of access request.");
        }
        return accessRequestMapper.mapToAccessRequestDto(
                accessRequestService.changeAccessRequestStatus(accessRequest, status)
        );
    }

    @DeleteMapping(headers = AUTH_HEADER)
    public void deleteUser(@RequestHeader(AUTH_HEADER) UUID sessionId) {
        LOGGER.info("DELETE:/v1/rest/users -> deleteUser()");
        sessionService.validateSession(sessionId);
        CommunicatorUser currentUser = sessionService.getCurrentUser(sessionId);
        userService.deleteUser(currentUser);
        chatRoomService.deleteUserChatRooms(currentUser.getId());
    }

}
