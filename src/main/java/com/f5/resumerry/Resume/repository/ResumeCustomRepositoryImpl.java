package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.selector.CategoryEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
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


    private final JPAQueryFactory queryFactory;

    public ResumeCustomRepositoryImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

    public List<ResumeDTO> viewResumesInMyPage(Long id) {
       return entityManager.createQuery("select new com.f5.resumerry.Resume.dto.ResumeDTO(r.id, r.title, r.contents, r.resumeRecommendList.size,r.resumeCommentList.size,r.viewCnt,r.modifiedDate, m.id, m.imageSrc, m.nickname,r.years) "
               + "from Resume r "
               + "join r.member m "
               + "where m.id = :id", ResumeDTO.class)
               .setParameter("id", id)
               .getResultList();
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


    // 5. sort 기준 - 추천, view , years, recent 별로 -> 게시글 출력
    // 1. categoty
    // 2. title 이 추가 되어있어야함
    // 3. startYear ~ endYear 해당 범위내에 있는 연차를 가져와야함
    // 4. hashtag는 일단 무시
    // 5. sort 기준 - 추천, view , years, recent 별로
    public List<Resume> findAllWithMember(String title, Integer startYear, Integer endYear, CategoryEnum category) {
       // resume의 모든 리스트를 반환한다. 이때 memebr와 join하여 모든 값을 가져온다. + hashtag 포함 1개
        return entityManager.createQuery("select r " +
                        "from Resume r " +
                        "join fetch r.member m " +
                        "where r.category = :category " +
                        "and r.title like concat('%', :title,'%') " +
                        "and r.isDelete = true " +
                        "and r.years between :startYear and :endYear " +
                        "order by r.createdDate desc " , Resume.class)
                .setParameter("title", title)
                .setParameter("startYear", startYear)
                .setParameter("endYear", endYear)
                .setParameter("category", category)
                .getResultList();
    }
    public List<Resume> findAllWithMemberByView(String title, Integer startYear, Integer endYear, CategoryEnum category) {
        // resume의 모든 리스트를 반환한다. 이때 memebr와 join하여 모든 값을 가져온다.
        return entityManager.createQuery("select r " +
                        "from Resume r " +
                        "join fetch r.member m " +
                        "where r.category = :category " +
                        "and r.title like concat('%', :title,'%') " +
                        "and r.isDelete = true " +
                        "and r.years between :startYear and :endYear " +
                        "order by r.viewCnt desc " , Resume.class)
                .setParameter("title", title)
                .setParameter("startYear", startYear)
                .setParameter("endYear", endYear)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Resume> findAllWithMemberByRecommend(String title, Integer startYear, Integer endYear, CategoryEnum category) {
        // resume의 모든 리스트를 반환한다. 이때 memebr와 join하여 모든 값을 가져온다.
        return entityManager.createQuery("select r " +
                        "from Resume r, ResumeHashtag  rh " +
                        "join fetch r.member m " +
                        "where r.category = :category " +
                        "and r.title like concat('%', :title,'%') " +
                        "and r.isDelete = true " +
                        "and r.years between :startYear and :endYear " +
                        "and rh.resumeId = r.id " +
                        "order by r.resumeRecommendList.size desc " , Resume.class)
                .setParameter("title", title)
                .setParameter("startYear", startYear)
                .setParameter("endYear", endYear)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Resume> findAllWithMemberByYears(String title, Integer startYear, Integer endYear, CategoryEnum category) {
        // resume의 모든 리스트를 반환한다. 이때 memebr와 join하여 모든 값을 가져온다.
        return entityManager.createQuery("select r " +
                        "from Resume r " +
                        "join fetch r.member m " +
                        "where r.category = :category " +
                        "and r.title like concat('%', :title,'%') " +
                        "and r.isDelete = true " +
                        "and r.years between :startYear and :endYear " +
                        "order by r.years desc " , Resume.class)
                .setParameter("title", title)
                .setParameter("startYear", startYear)
                .setParameter("endYear", endYear)
                .setParameter("category", category)
                .getResultList();
    }

    public Resume viewResume(Long resumeId) {
       return entityManager.createQuery("select r from Resume r " +
               "join fetch r.member " +
                               "where r.id = :resumeId"
               ,Resume.class)
               .setParameter("resumeId", resumeId)
               .getSingleResult();
    }

    public List<ResumeHashtag> findHashtag(Long resumeId) {
       return entityManager.createQuery(
               "select rh " +
                "from ResumeHashtag rh " +
               "join rh.hashtag h " +
                       "join rh.resume r " +
                       "where r.id = :resumeId", ResumeHashtag.class)
               .setParameter("resumeId", resumeId)
               .getResultList();
    }



}
