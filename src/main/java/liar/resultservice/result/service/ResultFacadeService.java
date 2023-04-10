package liar.resultservice.result.service;

import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.rank.PlayerRankingDto;
import liar.resultservice.result.service.dto.SaveResultDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface ResultFacadeService {

    /**
     * gameResult 관련 GameResult, player, playerResult 모두 저장
     */
    void saveAllResultOfGame(SaveResultDto saveResultDto);

    /**
     * playerRanking을 slice 방식으로 가져온다.
     */
    Slice<PlayerRankingDto> fetchPlayerRank(Pageable pageable);

    /**
     * client의 개별 게임 결과를 가져온다.
     */
    Slice<MyDetailGameResultDto> fetchMyDetailGameResult(MyDetailGameResultCond cond, Pageable pageable);
}
