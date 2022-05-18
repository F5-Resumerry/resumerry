package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.selector.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeCustomRepository {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Resume r set r.viewCnt = r.viewCnt+1 where r.id = :resumeId and r.memberId = :memberId")
    void viewCnt(Long memberId, Long resumeId);

    @Query("select (count(r) > 0) from Resume r where r.memberId = ?1 and r.id = ?2")
    boolean existScrapByMemberIdAndResumeId(Long memberId, Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Resume r set r.isDelete = 'Y' where r.id = :resumeId and r.memberId = :memberId")
    void updateIsDelete(@Param("memberId") Long memberId, @Param("resumeId") Long resumeId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Resume r set r.title = ?3 , r.contents = ?4 , r.category = ?5, r.years = ?6, r.fileLink = ?7 where r.id = ?2 and r.memberId = ?1")
    void updateResume(Long memberId, Long resumeId, String title, String contents, CategoryEnum category, Integer years, String fullFileNamePath);

    Optional<Resume> findById(Long id);

    List<Resume> findByMemberId(Long memberId);
}
