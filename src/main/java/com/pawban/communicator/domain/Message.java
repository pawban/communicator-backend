package com.pawban.communicator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
public class Message {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(length = 2000)
    private String text;

    @NotNull
    private LocalDateTime creationTime;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            foreignKey = @ForeignKey(name = "message_fkey_user")
    )
    private CommunicatorUser sender;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "chat_room_id",
            foreignKey = @ForeignKey(name = "message_fkey_chat_room")
    )
    private ChatRoom chatRoom;

    @Builder.Default
    @OneToMany(
            mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecipientMessage> recipientMessages = new ArrayList<>();


}
