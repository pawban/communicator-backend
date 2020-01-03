package com.pawban.communicator.domain;

import com.pawban.communicator.type.UserStatus;
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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "communicator_user_uk_session_id",
                columnNames = {"sessionId"}
        )
})
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
public class CommunicatorUser {

    public static final long SESSION_VALIDITY_PERIOD = 3000L;

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(length = 30)
    private String username;

    @NotNull
    @Column(length = 3)
    private String countryCode;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private UserStatus status = UserStatus.ACTIVE;

    @NotNull
    @Builder.Default
    private boolean visible = true;

    @NotNull
    @Builder.Default
    private UUID sessionId = UUID.randomUUID();

    @NotNull
    @Builder.Default
    private LocalDateTime validUntil = LocalDateTime.now().plusMinutes(SESSION_VALIDITY_PERIOD);

}
