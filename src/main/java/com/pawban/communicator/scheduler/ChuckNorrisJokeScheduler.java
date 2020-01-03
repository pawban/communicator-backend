package com.pawban.communicator.scheduler;

import com.pawban.communicator.client.ChuckNorrisClient;
import com.pawban.communicator.domain.ChatRoom;
import com.pawban.communicator.domain.Message;
import com.pawban.communicator.dto.JokeDto;
import com.pawban.communicator.exception.NotFoundException;
import com.pawban.communicator.service.ChatRoomService;
import com.pawban.communicator.service.CommunicatorUserService;
import com.pawban.communicator.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class ChuckNorrisJokeScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChuckNorrisJokeScheduler.class);

    private final CommunicatorUserService userService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final ChuckNorrisClient chuckNorrisClient;

    @Autowired
    public ChuckNorrisJokeScheduler(final CommunicatorUserService userService,
                                    final ChatRoomService chatRoomService,
                                    final MessageService messageService,
                                    final ChuckNorrisClient chuckNorrisClient) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
        this.chuckNorrisClient = chuckNorrisClient;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sendChuckNorrisJoke() {
        JokeDto joke = chuckNorrisClient.getRandomJoke().orElseThrow(
                () -> new NotFoundException("Chuck Norris roundhouse kicked me and I can't get any joke.")
        );
        Message jokeMessage = Message.builder()
                .text("Chuck Norris Fact #" + joke.getId() + ": " + joke.getJoke())
                .sender(userService.getChuckNorris())
                .build();
        LOGGER.info(jokeMessage.getText());
        Set<ChatRoom> chatRooms = chatRoomService.getChatRooms();
        chatRooms.forEach(
                chatRoom -> messageService.createMessage(jokeMessage.toBuilder()
                        .chatRoom(chatRoom)
                        .recipientMessages(new ArrayList<>())
                        .build()
                )
        );
    }

}
