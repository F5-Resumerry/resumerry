package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeCommentRepository extends JpaRepository<ResumeComment, Long> {
    List<ResumeComment> findByResume(Resume resume);

    Optional<ResumeComment> findById(Long resumeCommentId);
}
