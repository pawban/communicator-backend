package com.pawban.communicator.controller;

import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.domain.Message;
import com.pawban.communicator.dto.MessageDto;
import com.pawban.communicator.dto.NewMessageDto;
import com.pawban.communicator.mapper.MessageMapper;
import com.pawban.communicator.service.ChatRoomService;
import com.pawban.communicator.service.MessageService;
import com.pawban.communicator.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.pawban.communicator.service.SessionService.AUTH_HEADER;

@RestController
@RequestMapping("/v1/rest/messages")
@CrossOrigin("*")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private final SessionService sessionService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageController(final MessageService messageService,
                             final ChatRoomService chatRoomService,
                             final SessionService sessionService,
                             final MessageMapper messageMapper) {
        this.messageService = messageService;
        this.chatRoomService = chatRoomService;
        this.sessionService = sessionService;
        this.messageMapper = messageMapper;
    }

    @GetMapping(
            headers = AUTH_HEADER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MessageDto> getMessages(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                        @RequestParam(defaultValue = "true") boolean undeliveredOnly,
                                        @RequestParam(required = false) UUID chatRoomId) {
        LOGGER.info("GET:/v1/rest/messages -> getMessages()");
        sessionService.validateSession(sessionId);
        UUID currentUserId = sessionService.getCurrentUserId(sessionId);
        List<Message> messages;
        if (chatRoomId != null) {
            messages = messageService.getRecipientMessagesFromChatRoom(currentUserId, undeliveredOnly, chatRoomId);
        } else {
            messages = messageService.getRecipientMessages(currentUserId, undeliveredOnly);
        }
        return messageMapper.mapToMessageDtoList(messages);
    }

    @PostMapping(
            headers = AUTH_HEADER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageDto createMessage(@RequestHeader(AUTH_HEADER) UUID sessionId,
                                    @RequestBody NewMessageDto newMessageDto) {
        LOGGER.info("POST:/v1/rest/messages -> createMessages()");
        sessionService.validateSession(sessionId);
        CommunicatorUser sender = sessionService.getCurrentUser(sessionId);
        ChatRoom chatRoom = chatRoomService.getChatRoom(newMessageDto.getChatRoomId());
        return messageMapper.mapToMessageDto(
                messageService.createMessage(messageMapper.mapToMessage(newMessageDto, sender, chatRoom))
        );
    }

}
