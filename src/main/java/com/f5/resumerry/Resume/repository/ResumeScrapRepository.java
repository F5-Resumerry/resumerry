package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.ResumeScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeScrapRepository extends JpaRepository<ResumeScrap, Long> {
    Boolean existsByMemberAndResume(Member member, Resume resume);

    void deleteById(Long id);

    ResumeScrap findByResumeAndMember(Resume resume, Member member);

    Boolean existsByResume(Resume resume);
}