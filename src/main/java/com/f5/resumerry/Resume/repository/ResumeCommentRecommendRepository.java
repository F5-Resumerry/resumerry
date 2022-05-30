package com.f5.resumerry.Resume.repository;


import com.f5.resumerry.Resume.ResumeCommentRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResumeCommentRecommendRepository extends JpaRepository<ResumeCommentRecommend, Long> {
    void deleteByMemberIdAndResumeCommentId(Long memberId, Long commentId);
    boolean existsByMemberIdAndResumeCommentId(Long memberId, Long commentId);
}