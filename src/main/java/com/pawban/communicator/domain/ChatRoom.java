package com.pawban.communicator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(length = 7)
    private ChatRoomStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "ChatRoom_Members",
            joinColumns = {@JoinColumn(name = "chat_room_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id")}
    )
    private Set<User> members;

}
