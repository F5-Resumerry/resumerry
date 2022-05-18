package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeCommentRepository extends JpaRepository<ResumeComment, Long> {
    List<ResumeComment> findByResume(Resume resume);

    @Transactional
    @Modifying
    @Query(value = "UPDATE resume_comment r SET r.is_delete = 'Y' where r.resume_comment_id = :commentId", nativeQuery = true)
    Integer deleteResumeComment(Long commentId);

    Optional<ResumeComment> findById(Long resumeCommentId);

}
