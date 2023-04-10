package liar.resultservice.result.service;

import jakarta.transaction.Transactional;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.InitDb;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.controller.dto.request.VotedResultDto;
import liar.resultservice.result.controller.util.RequestMapperFactory;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import liar.resultservice.result.service.dto.SaveResultDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ResultFacadeServiceImplTest extends InitDb {

    @Autowired
    ResultFacadeService resultFacadeService;

    List<PlayerResultInfoDto> playerResultInfoDtos = new ArrayList<>();
    List<VotedResultDto>votedResultDtos = new ArrayList<>();
    SaveResultDto request;
    @Override
    @BeforeEach
    public void init() {
        super.init();
        createVotedResultDtos();
        createPlayerResultInfoDtos();
        request = RequestMapperFactory.mapper(new SaveResultRequest("gameId",
                GameRole.CITIZEN, playerResultInfoDtos, "roomId", "gameName",
                devUser1Id, topic.getId(), playerResultInfoDtos.size(), votedResultDtos));
    }

    @Test
    @Transactional
    @DisplayName("saveAllResultOfGame")
    public void saveAllResultOfGame_single() throws Exception {
        //given
        int count = 4;

        //when
        for (int i = 0; i < count; i++) {
            request = RequestMapperFactory.mapper(new SaveResultRequest(String.valueOf(i), GameRole.CITIZEN,
                    playerResultInfoDtos, "roomId", "gameName",
                    devUser1Id, topic.getId(), playerResultInfoDtos.size(), votedResultDtos));
            resultFacadeService.saveAllResultOfGame(request);
        }

        GameResult gameResult = gameResultRepository.findGameResultByGameId(request.getGameId());
        List<PlayerResult> playerResults = playerResultRepository.findPlayerResultsByGameResult(gameResult);
        GameResult findGameResult = gameResultRepository.findGameResultByGameId("1");
        Player playerByMember = playerRepository.findPlayerByMember(memberRepository.findByUserId(devUser1Id));

        //then
        assertThat(findGameResult).isNotNull();
        assertThat(findGameResult.getGameName()).isNotNull();
        assertThat(gameResult.getHostId()).isEqualTo(devUser1Id);
        assertThat(playerResults.size()).isEqualTo(4);
        assertThat(playerByMember.getExp()).isEqualTo(10L * 4);
    }

    @Test
    @DisplayName("saveAllResultOfGame_multiThread")
    public void saveAllResultOfGame_multiThread() throws Exception {
        //given
        int threadCount = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try{
                    SaveResultDto resultTest = getSaveResultDto(finalIdx, topic);
                    resultFacadeService.saveAllResultOfGame(resultTest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        GameResult gameResult1 = gameResultRepository.findGameResultByGameId("0");
        GameResult gameResult2 = gameResultRepository.findGameResultByGameId("1");
        GameResult gameResult3 = gameResultRepository.findGameResultByGameId("2");
        GameResult gameResult4 = gameResultRepository.findGameResultByGameId("3");
        List<PlayerResult> playerResults1 = playerResultRepository.findPlayerResultsByGameResult(gameResult1);
        List<PlayerResult> playerResults2 = playerResultRepository.findPlayerResultsByGameResult(gameResult2);
        List<PlayerResult> playerResults3 = playerResultRepository.findPlayerResultsByGameResult(gameResult3);
        List<PlayerResult> playerResults4 = playerResultRepository.findPlayerResultsByGameResult(gameResult4);
        Player player1ByMember = playerRepository.findPlayerByMember(memberRepository.findByUserId(devUser1Id));
        Player player2ByMember = playerRepository.findPlayerByMember(memberRepository.findByUserId(devUser2Id));

        //then
        assertThat(gameResult1.getHostId()).isEqualTo(devUser1Id);
        assertThat(playerResults1.size()).isEqualTo(4);
        assertThat(gameResult2.getHostId()).isEqualTo(devUser1Id);
        assertThat(playerResults2.size()).isEqualTo(4);
        assertThat(gameResult3.getHostId()).isEqualTo(devUser1Id);
        assertThat(playerResults3.size()).isEqualTo(4);
        assertThat(gameResult4.getHostId()).isEqualTo(devUser1Id);
        assertThat(playerResults4.size()).isEqualTo(4);
        assertThat(player1ByMember.getExp()).isEqualTo(10L * 4);
        assertThat(player2ByMember.getExp()).isEqualTo(15L * 4);

    }

    @Test
    @DisplayName("saveAllResultOfGame_multiThread 10개")
    public void saveAllResultOfGame_multiThread_more() throws Exception {
        //given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try{
                    SaveResultDto resultTest = getSaveResultDto(finalIdx, topic);
                    resultFacadeService.saveAllResultOfGame(resultTest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        GameResult gameResult1 = gameResultRepository.findGameResultByGameId("0");
        List<PlayerResult> playerResults1 = playerResultRepository.findPlayerResultsByGameResult(gameResult1);
        Player player1ByMember = playerRepository.findPlayerByMember(memberRepository.findByUserId(devUser1Id));
        Player player2ByMember = playerRepository.findPlayerByMember(memberRepository.findByUserId(devUser2Id));

        //then
        assertThat(gameResult1.getHostId()).isEqualTo(devUser1Id);
        assertThat(playerResults1.size()).isEqualTo(4);
        assertThat(player1ByMember.getExp()).isEqualTo(49L * 10);
        assertThat(player2ByMember.getExp()).isEqualTo(15L * 10);

    }

    private void createPlayerResultInfoDtos() {
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser1Id, GameRole.LIAR, false));
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser2Id, GameRole.CITIZEN, false));
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser3Id, GameRole.CITIZEN, false));
        playerResultInfoDtos.add(new PlayerResultInfoDto(devUser4Id, GameRole.CITIZEN, false));
    }

    private void createVotedResultDtos() {
        votedResultDtos.add(new VotedResultDto(devUser2Id, Arrays.asList(devUser4Id), 1));
        votedResultDtos.add(new VotedResultDto(devUser1Id, Arrays.asList(devUser2Id, devUser3Id), 2));
        votedResultDtos.add(new VotedResultDto(devUser3Id, Arrays.asList(devUser1Id), 1));
        votedResultDtos.add(new VotedResultDto(devUser4Id, Arrays.asList(), 0));
    }

    private SaveResultDto getSaveResultDto(int finalIdx, Topic savedTopic) {
        SaveResultDto resultTest = RequestMapperFactory
                .mapper(new SaveResultRequest(String.valueOf(finalIdx), GameRole.CITIZEN, playerResultInfoDtos,
                "roomId", "gameName", devUser1Id, savedTopic.getId(), playerResultInfoDtos.size(),
                        votedResultDtos));
        return resultTest;
    }
}