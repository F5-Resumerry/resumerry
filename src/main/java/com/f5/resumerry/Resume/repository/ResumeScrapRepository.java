package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.ResumeScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeScrapRepository extends JpaRepository<ResumeScrap, Long> {
    Boolean existsByResumeIdAndMemberId(Long resumeId, Long memberId);

    void deleteById(Long id);

    ResumeScrap findByResumeIdAndMemberId(Long resumeId, Long memberId);

}