package com.pawban.communicator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Builder
@Getter
public class MessageToReceiver {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Builder.Default
    private Boolean delivered;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

}
