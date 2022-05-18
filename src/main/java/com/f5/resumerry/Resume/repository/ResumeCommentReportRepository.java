package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.ResumeCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeCommentReportRepository extends JpaRepository<ResumeCommentReport, Long> {
    void deleteByMemberIdAndResumeCommentId(Long memberId, Long commentId);
    boolean existsByMemberIdAndResumeCommentId(Long memberId, Long commentId);
}