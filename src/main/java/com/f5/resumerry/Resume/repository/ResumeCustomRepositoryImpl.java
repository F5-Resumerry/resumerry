package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.dto.ResumeDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.security.PublicKey;
import java.util.List;

public class ResumeCustomRepositoryImpl implements ResumeCustomRepository {
    @Autowired
    private EntityManager entityManager;
   public List<ResumeDTO> viewResumesInMyPage(Long id) {
       return entityManager.createQuery("select new com.f5.resumerry.Resume.dto.ResumeDTO(r.id, r.title, r.contents, r.resumeRecommendList.size,r.resumeCommentList.size,r.viewCnt,r.modifiedDate,r.hashtag, m.id, m.imageSrc, m.nickname,r.years ) "
               + "from Resume r "
               + "join r.member m "
               + "where m.id = :id", ResumeDTO.class)
               .setParameter("id", id)
               .getResultList();
   }

}
