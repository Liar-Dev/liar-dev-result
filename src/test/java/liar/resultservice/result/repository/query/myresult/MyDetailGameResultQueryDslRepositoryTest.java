package liar.resultservice.result.repository.query.myresult;

import jakarta.transaction.Transactional;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MyDetailGameResultQueryDslRepositoryTest {

    @Autowired
    MyDetailGameResultQueryDslRepository myDetailGameResultQueryDslRepository;
    @Autowired
    GameResultRepository gameResultRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PlayerResultRepository playerResultRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TopicRepository topicRepository;

    private static final String DEV_USER_ID = "0003c0c8-4af1-4bd0-bdb8-62ceb8308fa5";

    @BeforeEach
    public void init() {
    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 onlyLastViews를 테스트 한다.")
    public void fetchMyDetailGameResult_onlyLastViews() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(DEV_USER_ID, true, null,
                null, null);

        long before = System.currentTimeMillis();
        Slice<MyDetailGameResultDto> myDetailGameResultDtos =
                myDetailGameResultQueryDslRepository.fetchMyDetailGameResult(cond, page);
        long after = System.currentTimeMillis();

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0)).isNotNull();
        System.out.println("Total Query Time = " + (after - before) + "ms");
    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 onlyWin을 테스트 한다.")
    public void fetchMyDetailGameResult_onlyWin() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(DEV_USER_ID, null, true,
                null, null);

        long before = System.currentTimeMillis();

        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository
                .fetchMyDetailGameResult(cond, page);

        long after = System.currentTimeMillis();
        //then
        assertThat(myDetailGameResultDtos.getContent().get(0)).isNotNull();
        System.out.println("Total Query Time = " + (after - before) + "ms");
    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 onlyLose을 테스트 한다.")
    public void fetchMyDetailGameResult_onlyLose() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        long before = System.currentTimeMillis();
        MyDetailGameResultCond cond = new MyDetailGameResultCond(DEV_USER_ID, null, null,
                true, null);
        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository.fetchMyDetailGameResult(cond, page);
        long after = System.currentTimeMillis();

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0)).isNotNull();
        System.out.println("Total Query Time = " + (after - before) + "ms");

    }

    @Test
    @DisplayName("fetchMyDetailGameResult의 searchGameName을 테스트 한다.")
    public void fetchMyDetailGameResult_searchGameName() throws Exception {
        //given
        Pageable page = PageRequest.of(0, 10);

        //when
        MyDetailGameResultCond cond = new MyDetailGameResultCond(DEV_USER_ID, null, null,
                null, "ㄱㄱ");

        long before = System.currentTimeMillis();
        Slice<MyDetailGameResultDto> myDetailGameResultDtos = myDetailGameResultQueryDslRepository
                .fetchMyDetailGameResult(cond, page);
        long after = System.currentTimeMillis();

        //then
        assertThat(myDetailGameResultDtos.getContent().get(0)).isNotNull();
        System.out.println("Total Query Time = " + (after - before) + "ms");
    }

}