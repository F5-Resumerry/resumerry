package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.dto.ViewResumeDTO;
import com.f5.resumerry.selector.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
@Transactional
@Repository
public class ResumeCustomRepositoryImpl implements ResumeCustomRepository {
    @Autowired
    private EntityManager entityManager;
   public List<ResumeDTO> viewResumesInMyPage(Long id) {
       return entityManager.createQuery("select new com.f5.resumerry.Resume.dto.ResumeDTO(r.id, r.title, r.contents, r.resumeRecommendList.size,r.resumeCommentList.size,r.viewCnt,r.modifiedDate,r.hashtag, m.id, m.imageSrc, m.nickname,r.years) "
               + "from Resume r "
               + "join r.member m "
               + "where m.id = :id", ResumeDTO.class)
               .setParameter("id", id)
               .getResultList();
   }

    public ViewResumeDTO viewResumeMine(Long memberId, Long resumeId) {
        return entityManager.createQuery("select new com.f5.resumerry.Resume.dto.ViewResumeDTO(r.id, r.title, r.contents, r.resumeRecommendList.size, r.resumeCommentList.size, r.viewCnt,r.modifiedDate, m.id, m.imageSrc, m.nickname, r.years, true, false) "
                + "from Resume r join r.member m "
                + "where r.id = :resumeId "
                , ViewResumeDTO.class)
                .setParameter("resumeId", resumeId)
                .getSingleResult();
    }



    // 스크랩한 이력이 없는 경우
    public ViewResumeDTO noOwnerResumeAndNoScrap(Long tokenId, Long resumeId) {
       return entityManager.createQuery("select new com.f5.resumerry.Resume.dto.ViewResumeDTO(r.id, r.title, r.contents, r.resumeRecommendList.size, r.resumeCommentList.size, r.viewCnt,r.modifiedDate, m.id, m.imageSrc, m.nickname, r.years, false, false ) "
                               + "from Resume r join r.member m "
                               + "where r.id = :resumeId "
                            , ViewResumeDTO.class)
               .setParameter("resumeId", resumeId)
               .getSingleResult();
    }

    public ViewResumeDTO noOwnerResumeCanScrap(Long tokenId, Long resumeId){
        return entityManager.createQuery("select new com.f5.resumerry.Resume.dto.ViewResumeDTO(r.id, r.title, r.contents, r.resumeRecommendList.size, r.resumeCommentList.size, r.viewCnt,r.modifiedDate, m.id, m.imageSrc, m.nickname, r.years, false, true ) "
                                + "from Resume r join r.member m "
                                + "where r.id = :resumeId "
                        , ViewResumeDTO.class)
                .setParameter("resumeId", resumeId)
                .getSingleResult();
    }

    public void uploadResume(Long id, String fullFileLink, String title, String contents, CategoryEnum category, Integer years) {
       entityManager.createNativeQuery("insert resume(category, contents, file_link, title, years, member_id) values (?, ?, ?, ?, ?, ?)")
               .setParameter(1, String.valueOf(category))
               .setParameter(2, contents)
               .setParameter(3, fullFileLink)
               .setParameter(4, title)
               .setParameter(5, years)
               .setParameter(6, id)
               .executeUpdate();
    }

//    public Boolean existScrapByMemberIdAndResumeId(Long memberId, Long resumeId) {
//       return entityManager.createQuery("select rs from ResumeScrap rs where rs.memberId = :memberId and rs.resumeId = :resumeId")
//               .
//    }




}
