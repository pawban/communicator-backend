package com.pawban.communicator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class RecipientMessage {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Builder.Default
    private boolean delivered = false;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "message_id",
            foreignKey = @ForeignKey(name = "recipient_message_fkey_message")
    )
    private Message message;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "recipient_id",
            foreignKey = @ForeignKey(name = "recipient_message_fkey_user")
    )
    private CommunicatorUser recipient;

}
