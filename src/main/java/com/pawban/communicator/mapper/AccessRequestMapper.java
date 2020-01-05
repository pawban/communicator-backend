package com.pawban.communicator.mapper;

import com.pawban.communicator.domain.AccessRequest;
import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.CommunicatorUser;
import com.pawban.communicator.dto.AccessRequestDto;
import com.pawban.communicator.dto.NewAccessRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AccessRequestMapper {

    private final CommunicatorUserMapper userMapper;
    private final ChatRoomMapper chatRoomMapper;

    @Autowired
    public AccessRequestMapper(final CommunicatorUserMapper userMapper,
                               final ChatRoomMapper chatRoomMapper) {
        this.userMapper = userMapper;
        this.chatRoomMapper = chatRoomMapper;
    }

    public AccessRequest mapToAccessRequest(final NewAccessRequestDto newAccessRequestDto,
                                            final CommunicatorUser sender,
                                            final ChatRoom chatRoom) {
        return AccessRequest.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .request(newAccessRequestDto.getRequest())
                .build();
    }

    public Set<AccessRequestDto> mapToAccessRequestDtoSet(final Set<AccessRequest> accessRequests) {
        return accessRequests.stream()
                .map(this::mapToAccessRequestDto)
                .collect(Collectors.toSet());
    }

    public AccessRequestDto mapToAccessRequestDto(final AccessRequest accessRequest) {
        return new AccessRequestDto(
                accessRequest.getId(),
                userMapper.mapToUserDto(accessRequest.getSender()),
                chatRoomMapper.mapToChatRoomDto(accessRequest.getChatRoom()),
                accessRequest.getRequest(),
                accessRequest.getStatus()
        );
    }

}
