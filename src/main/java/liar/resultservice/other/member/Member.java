package liar.resultservice.other.member;

import jakarta.persistence.*;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@Table(indexes = {@Index(name = "member_user_id_index", columnList = "userId")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Serializable {

    @Id
    @Column(name = "member_id")
    private String id;
    private String userId;
    private String username;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private Player player;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PlayerResult> playerResults = new ArrayList<>();

    public Member(String userId, String username) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.username = username;
    }

    public static Member of(String userId, String username) {
        return new Member(userId, username);
    }
}
