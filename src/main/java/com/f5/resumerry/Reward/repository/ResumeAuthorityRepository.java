package com.f5.resumerry.Reward.repository;

import com.f5.resumerry.Reward.ResumeAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResumeAuthorityRepository extends JpaRepository<ResumeAuthority,Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into resume_authority(member_id, resume_id) values (:memberId, :resumeId)", nativeQuery = true)
    public void insertResumeAuthority(@Param("memberId") Long memberId,@Param("resumeId") Long resumeId);

    Boolean existsByMemberIdAndResumeId(Long memberId, Long resumeId);
}
