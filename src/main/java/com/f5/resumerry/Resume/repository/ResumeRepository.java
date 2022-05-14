package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.dto.ResumeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeCustomRepository {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Resume r set r.viewCnt = r.viewCnt+1 where r.id = :resumeId and r.memberId = :memberId")
    void viewCnt(Long memberId, Long resumeId);

    @Query("select (count(r) > 0) from Resume r where r.memberId = ?1 and r.id = ?2")
    boolean existScrapByMemberIdAndResumeId(Long memberId, Long id);



//    @Query(value = "select case when rs.memberId = ?1 and rs.resumeId = ?2 then true else false end from ResumeScrap rs")
//    Boolean existScrapByMemberIdAndResumeId(Long memberId, Long resumeId);

}
