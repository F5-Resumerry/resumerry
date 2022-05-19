package com.f5.resumerry.Reward.repository;

import com.f5.resumerry.Reward.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface TokenHistoryRepository extends JpaRepository<TokenHistory, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into TokenHistory(count, reason, member_id, resume_id) values (:c, :tokenUsing, :memberId, :resuemId)", nativeQuery = true)
    void insertTokenHistory(Long memberId, Long resumeId, String tokenUsing, Integer c);
}
