package com.pawban.communicator.domain;

import com.pawban.communicator.type.AccessRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class AccessRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            foreignKey = @ForeignKey(name = "access_request_fkey_sender")
    )
    private CommunicatorUser sender;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "chat_room_id",
            foreignKey = @ForeignKey(name = "access_request_fkey_chat_room")
    )
    private ChatRoom chatRoom;

    @NotNull
    @Column(length = 2000)
    private String request;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AccessRequestStatus status = AccessRequestStatus.PENDING;

    @NotNull
    @Builder.Default
    private boolean delivered = false;

    @NotNull
    @Builder.Default
    private LocalDateTime creationTime = LocalDateTime.now();

}
