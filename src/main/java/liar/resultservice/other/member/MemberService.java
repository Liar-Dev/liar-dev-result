package liar.resultservice.other.member;

import liar.resultservice.other.member.dao.UserIdOnly;
import liar.resultservice.other.member.dao.UserNameOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public List<UserIdOnly> findByUsername(String username) {
        return memberRepository.findProjectionByUsername(username);
    }

    public UserNameOnly findUsernameById(String userId) {
        return memberRepository.findProjectionByUserId(userId);
    }


}
