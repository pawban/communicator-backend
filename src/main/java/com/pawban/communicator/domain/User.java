package com.pawban.communicator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Builder
@Getter
public class User {

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
    @Column(length = 7)
    private UserStatus status;

    @NotNull
    @Builder.Default
    private LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

}
