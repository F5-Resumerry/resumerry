package com.f5.resumerry.Reward.repository;

import com.f5.resumerry.Reward.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface TokenHistoryRepository extends JpaRepository<TokenHistory, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into token_history(token_cnt, reason, member_id, resume_id) values (:c, :tokenUsing, :memberId, :resumeId)", nativeQuery = true)
    void insertTokenHistory(@Param("memberId") Long memberId, @Param("resumeId") Long resumeId, @Param("tokenUsing") String tokenUsing, @Param("c") Long c);
}
