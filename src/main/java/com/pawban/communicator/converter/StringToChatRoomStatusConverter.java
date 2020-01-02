package com.pawban.communicator.converter;

import com.pawban.communicator.type.ChatRoomStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToChatRoomStatusConverter implements Converter<String, ChatRoomStatus> {

    @Override
    public ChatRoomStatus convert(String source) {
        return ChatRoomStatus.valueOf(source.toUpperCase());
    }

}
