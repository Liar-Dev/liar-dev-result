package liar.resultservice.result;


import liar.resultservice.other.member.Member;
import liar.resultservice.other.member.MemberRepository;
import liar.resultservice.other.topic.Topic;
import liar.resultservice.other.topic.TopicRepository;
import liar.resultservice.result.repository.GameResultRepository;
import liar.resultservice.result.repository.PlayerRepository;
import liar.resultservice.result.repository.PlayerResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class InitDb {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected TopicRepository topicRepository;

    @Autowired
    protected GameResultRepository gameResultRepository;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected PlayerResultRepository playerResultRepository;

    protected String hostId = "159b49cd-78d2-4b2d-8aa2-5b986b623251";
    protected String devUser1Id = "fe557c5a-15c2-41f5-83a4-c87d8061c45a";
    protected String devUser2Id = "2b970366-7a85-41da-b636-628969c45e88";
    protected String devUser3Id = "0fa66a79-f4cc-4bf4-8a3e-f2cd81cf82c5";
    protected String devUser4Id = "b2886e51-6444-4b1b-94e0-a34ab1dbbb3b";
    protected String devUser5Id = "2d972b43-4d2d-4383-b1aa-32fabba42185";
    protected String devUser6Id = "a02814cf-d529-43a4-b2d9-941b35b3b8fa";
    protected static Topic topic;

    protected List<String> membersId = Arrays.asList(hostId, devUser1Id, devUser2Id, devUser3Id,
            devUser4Id, devUser5Id, devUser6Id);

    @BeforeEach
    public void init() {
        for (int i = 0; i < membersId.size(); i++) {
            memberRepository.save(Member.of(membersId.get(i), "KOSE" + (i + 1)));
        }
        topic = topicRepository.save(new Topic("test"));
    }

    @AfterEach
    public void tearDown() {
        playerRepository.deleteAll();
        playerResultRepository.deleteAll();
        gameResultRepository.deleteAll();
        topicRepository.deleteAll();
        memberRepository.deleteAll();
    }

}
