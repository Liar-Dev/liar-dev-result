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
public class PlayerResult extends BaseEntity implements Persistable<String>  {

    @Id
    @Column(name = "player_result_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_result_id")
    private GameResult gameResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private GameRole gameRole;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean answers;
    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isWin;
    private Long exp;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return super.getCreatedAt() == null;
    }

    @Builder
    public PlayerResult(GameResult gameResult, Member member, GameRole gameRole, Boolean answers,
                        Boolean isWin, Long exp) {
        this.id = UUID.randomUUID().toString();
        this.gameResult = gameResult;
        this.member = member;
        this.gameRole = gameRole;
        this.answers = answers;
        this.isWin = isWin;
        this.exp = exp;
    }
}
