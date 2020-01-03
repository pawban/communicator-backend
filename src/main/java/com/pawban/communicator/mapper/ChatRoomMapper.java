package com.pawban.communicator.mapper;

import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.dto.ChatRoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatRoomMapper {

    private final CommunicatorUserMapper userMapper;

    @Autowired
    public ChatRoomMapper(final CommunicatorUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ChatRoom mapToChatRoom(final ChatRoomDto chatRoomDto) {
        return ChatRoom.builder()
                .name(chatRoomDto.getName())
                .status(chatRoomDto.getStatus())
                .build();
    }

    public List<ChatRoomDto> mapToChatRoomDtoList(final Collection<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .map(this::mapToChatRoomDto)
                .collect(Collectors.toList());
    }

    public ChatRoomDto mapToChatRoomDto(final ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .status(chatRoom.getStatus())
                .owner(userMapper.mapToUserDto(chatRoom.getOwner()))
                .build();
    }

}
