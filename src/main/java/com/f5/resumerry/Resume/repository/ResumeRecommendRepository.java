package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResumeRecommendRepository extends JpaRepository<ResumeRecommend, Long>{

    void deleteById(Long id);

    ResumeRecommend findByResumeIdAndMemberId(Long ResumeId, Long memberId);

    Boolean existsByResume(Resume resume);
}