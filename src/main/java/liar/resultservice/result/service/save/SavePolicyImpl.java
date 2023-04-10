package liar.resultservice.result.service.save;

import jakarta.persistence.LockModeType;
import liar.resultservice.exception.exception.NotFoundGameResultException;
import liar.resultservice.exception.exception.NotFoundTopicException;
import liar.resultservice.exception.exception.NotFoundUserException;
import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import liar.resultservice.result.service.dto.SaveResultDto;
import liar.resultservice.result.service.exp.ExpPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SavePolicyImpl implements SavePolicy {

    private final TopicRepository topicRepository;
    private final GameResultRepository gameResultRepository;
    private final ExpPolicy expPolicy;
    private final PlayerRepository playerRepository;
    private final PlayerResultRepository playerResultRepository;
    private final MemberRepository memberRepository;


    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    /**
     * gameResult를 저장하고 id를 반환
     * @return GameResult
     */
    @Override
    public GameResult saveGameResult(SaveResultDto dto) {
        Topic topic = topicRepository.findById(dto.getTopicId()).orElseThrow(NotFoundTopicException::new);
        GameResult gameResult = gameResultRepository.save(
                GameResult.builder()
                        .gameId(dto.getGameId())
                        .gameName(dto.getGameName())
                        .hostId(dto.getHostId())
                        .roomId(dto.getRoomId())
                        .topic(topic)
                        .winner(dto.getWinner())
                        .totalUsers(dto.getTotalUserCnt())
                        .build());
        return gameResult;
    }

    /**
     * player를 업데이트
     */
    @Override
    public void updatePlayer(GameResult gameResult, Player player, GameRole playerRole, Long exp) {
        player.levelUp(expPolicy.nextLevel(player.updateExp(exp)));
        player.updateGameResult(playerRole == gameResult.getWinner());
    }

    /**
     * playerResult를 저장하고 id를 반환
     *
     * @return String
     */
    @Override
    public String savePlayerResult(String gameResultId, PlayerResultInfoDto dto, Long exp) {

        GameResult gameResult = gameResultRepository.findById(gameResultId).orElseThrow(NotFoundGameResultException::new);
        Member member = memberRepository.findByUserId(dto.getUserId());
        if (member == null) new NotFoundUserException();

        return playerResultRepository.save(PlayerResult.builder()
                .gameResult(gameResult)
                .member(member)
                .answers(dto.getAnswers())
                .isWin(dto.getGameRole() == gameResult.getWinner())
                .gameRole(dto.getGameRole())
                .exp(exp)
                .build()).getId();
    }

    @Override
    public Player getPlayer(PlayerResultInfoDto dto) {

        if (players.size() >= 100) {
            players.clear();
        }

        return players.computeIfAbsent(dto.getUserId(), userId -> {
            Member member = getMember(dto);
            Player player = playerRepository.findWithMemberForUpdate(member);
            if (player == null) return playerRepository.saveAndFlush(Player.of(member));
            return player;
        });
    }

    @Transactional(readOnly = true)
    public Member getMember(PlayerResultInfoDto dto) {
        Member member = memberRepository.findByUserId(dto.getUserId());
        if (member == null) {
            throw new NotFoundUserException();
        }
        return member;
    }

}
