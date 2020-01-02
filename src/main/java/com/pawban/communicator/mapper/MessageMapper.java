package com.pawban.communicator.mapper;

import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.domain.Message;
import com.pawban.communicator.dto.MessageDto;
import com.pawban.communicator.dto.NewMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    private final ChatRoomMapper chatRoomMapper;

    @Autowired
    public MessageMapper(final ChatRoomMapper chatRoomMapper) {
        this.chatRoomMapper = chatRoomMapper;
    }

    public Message mapToMessage(final NewMessageDto newMessageDto,
                                final CommunicatorUser sender,
                                final ChatRoom chatRoom) {
        return Message.builder()
                .text(newMessageDto.getText())
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
    }

    public List<MessageDto> mapToMessageDtoList(final List<Message> messages) {
        return messages.stream()
                .map(this::mapToMessageDto)
                .collect(Collectors.toList());
    }

    public MessageDto mapToMessageDto(final Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .sender(message.getSender().getUsername())
                .text(message.getText())
                .creationTime(message.getCreationTime())
                .chatRoomId(message.getChatRoom().getId())
                .build();
    }

}
