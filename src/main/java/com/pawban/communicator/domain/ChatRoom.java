package com.pawban.communicator.domain;

import com.pawban.communicator.type.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class ChatRoom {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ChatRoomStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = "chat_room_fkey_user")
    )
    private CommunicatorUser owner;

    @ManyToMany
    @JoinTable(
            name = "chat_room_members",
            joinColumns = {@JoinColumn(
                    name = "chat_room_id",
                    foreignKey = @ForeignKey(name = "chat_room_members_fkey_chat_room")
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "member_id",
                    foreignKey = @ForeignKey(name = "chat_room_members_fkey_user")
            )}
    )
    private Set<CommunicatorUser> members;

    @Builder.Default
    @OneToMany(
            mappedBy = "chatRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Message> messages = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "chatRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<AccessRequest> accessRequests = new HashSet<>();

}
