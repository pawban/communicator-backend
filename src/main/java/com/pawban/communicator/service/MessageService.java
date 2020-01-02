package com.pawban.communicator.service;

import com.pawban.communicator.domain.Message;
import com.pawban.communicator.domain.RecipientMessage;
import com.pawban.communicator.repository.MessageRepository;
import com.pawban.communicator.repository.RecipientMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RecipientMessageRepository recipientMessageRepository;

    @Autowired
    public MessageService(final MessageRepository messageRepository,
                          final RecipientMessageRepository recipientMessageRepository) {
        this.messageRepository = messageRepository;
        this.recipientMessageRepository = recipientMessageRepository;
    }

    public Message createMessage(final Message newMessage) {
        newMessage.setCreationTime(LocalDateTime.now());
        newMessage.getRecipientMessages().addAll(createRecipientMessagesForMessage(newMessage));
        return messageRepository.save(newMessage);
    }

    private List<RecipientMessage> createRecipientMessagesForMessage(final Message message) {
        return message.getChatRoom().getMembers().stream()
                .map(member -> RecipientMessage.builder()
                        .message(message)
                        .recipient(member)
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<Message> getRecipientMessages(final UUID recipientId,
                                              final boolean undeliveredOnly) {
        List<RecipientMessage> recipientMessages;
        if (undeliveredOnly) {
            recipientMessages = recipientMessageRepository
                    .findAllByDeliveredFalseAndRecipientIdOrderByMessageCreationTimeAsc(recipientId);
        } else {
            recipientMessages = recipientMessageRepository
                    .findAllByRecipientIdOrderByMessageCreationTimeAsc(recipientId);
        }
        markMessagesRead(recipientMessages);
        return recipientMessages.stream()
                .map(RecipientMessage::getMessage)
                .collect(Collectors.toList());
    }

    private void markMessagesRead(final List<RecipientMessage> recipientMessages) {
        recipientMessages.forEach(recipientMessage -> {
            recipientMessage.setDelivered(true);

        });
        recipientMessageRepository.saveAll(recipientMessages);
    }

    public List<Message> getRecipientMessagesFromChatRoom(final UUID recipientId,
                                                          final boolean undeliveredOnly,
                                                          final UUID chatRoomId) {
        List<RecipientMessage> recipientMessages;
        if (undeliveredOnly) {
            recipientMessages = recipientMessageRepository
                    .findAllByDeliveredFalseAndRecipientIdAndMessageChatRoomIdOrderByMessageCreationTimeAsc(
                            recipientId,
                            chatRoomId
                    );
        } else {
            recipientMessages = recipientMessageRepository
                    .findAllByRecipientIdAndMessageChatRoomIdOrderByMessageCreationTimeAsc(recipientId, chatRoomId);
        }
        markMessagesRead(recipientMessages);
        return recipientMessages.stream()
                .map(RecipientMessage::getMessage)
                .collect(Collectors.toList());
    }

//    public void broadcastMessage(final Message message, final)

}
