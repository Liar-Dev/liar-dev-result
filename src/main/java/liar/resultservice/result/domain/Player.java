package liar.resultservice.result.domain;

import jakarta.persistence.*;
import liar.resultservice.other.member.Member;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "player_exp_index", columnList = "exp"),
        @Index(name = "player_member_index", columnList = "member_id")
})
public class Player extends BaseEntity implements Persistable<String>  {

    @Id
    @Column(name = "player_id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    private Long wins;
    private Long loses;
    private Long totalGames;

    private Long exp;

    @Enumerated(value = EnumType.STRING)
    private Level level;

    private boolean visibleGameResult;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return super.getCreatedAt() == null;
    }


    @Builder
    public Player(Member member, Long wins, Long loses, Long totalGames, Long exp, Level level) {
        this.id = UUID.randomUUID().toString();
        this.member = member;
        this.wins = wins;
        this.loses = loses;
        this.totalGames = totalGames;
        this.exp = exp;
        this.level = level;
        this.visibleGameResult = true;
    }

    public static Player of(Member member) {
        return Player.builder()
                .member(member)
                .wins(0L)
                .loses(0L)
                .totalGames(0L)
                .exp(0L)
                .level(Level.BRONZE1)
                .build();
    }

    public Long updateExp(Long exp) {
        return this.exp += exp;
    }

    public Level levelUp(Level level) {
        return this.level = level;
    }

    public long updateGameResult(boolean win) {
        if (win) wins++; else loses++;
        return ++totalGames;
    }

    public boolean updateVisibleGameResult() {
        if (isVisibleGameResult()) return this.visibleGameResult = false;
        return this.visibleGameResult = true;
    }

}
