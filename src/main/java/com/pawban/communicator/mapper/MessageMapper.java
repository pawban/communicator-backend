package com.pawban.communicator.mapper;

import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.domain.Message;
import com.pawban.communicator.dto.MessageDto;
import com.pawban.communicator.dto.NewMessageDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

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
        return new MessageDto(
                message.getId(),
                message.getSender().getUsername(),
                message.getText(),
                message.getCreationTime(),
                message.getChatRoom().getId()
        );
    }

}
