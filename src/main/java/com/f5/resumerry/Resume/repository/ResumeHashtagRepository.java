package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.ResumeHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeHashtagRepository extends JpaRepository<ResumeHashtag, Long> {
    List<ResumeHashtag> findByResumeId(Long resumeId);

}