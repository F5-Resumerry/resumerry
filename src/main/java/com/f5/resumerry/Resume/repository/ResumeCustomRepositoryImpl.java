package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.selector.CategoryEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import javax.persistence.EntityManager;
import java.util.List;

import static com.f5.resumerry.Member.domain.entity.QMember.member;
import static com.f5.resumerry.Resume.QResume.resume;
import static com.f5.resumerry.Resume.QResumeComment.resumeComment;
import static com.f5.resumerry.Resume.QResumeRecommend.resumeRecommend;
import static com.f5.resumerry.Resume.QResumeScrap.resumeScrap;

@Transactional
@Repository
public class ResumeCustomRepositoryImpl implements ResumeCustomRepository {
    @Autowired
    private EntityManager entityManager;


    private final JPAQueryFactory queryFactory;

    public ResumeCustomRepositoryImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

    public List<FilterViewResumeDTO> viewResumesInMyPage(Long id) {
        return queryFactory
                .selectDistinct(Projections.bean(FilterViewResumeDTO.class,
                        resume.id.as("resumeId")
                        , resume.title
                        , resume.contents
                        , resumeRecommend.count().intValue().as("recommendCnt")
                        , resumeComment.count().intValue().as("commentCnt")
                        , resume.viewCnt
                        , resume.modifiedDate
                        , resume.isLock
                        , member.id.as("memberId")
                        , member.imageSrc
                        , member.nickname
                        , member.years
                ))
                .from(resume)
                .innerJoin(member).on(member.id.eq(resume.memberId))
                .leftJoin(resumeComment).on(resumeComment.resumeId.eq(resume.id))
                .leftJoin(resumeRecommend).on(resumeRecommend.resumeId.eq(resume.id))
                .groupBy(resume.id, resumeComment.id)
                .where(resume.isDelete.eq(true).and(member.id.eq(id)))
                .fetch();
   }
    public List<FilterViewResumeDTO> viewScrapResumesInMyPage(Long memberId) {
        return queryFactory
                .selectDistinct(Projections.bean(FilterViewResumeDTO.class,
                        resume.id.as("resumeId")
                        , resume.title
                        , resume.contents
                        , resumeRecommend.count().intValue().as("recommendCnt")
                        , resumeComment.count().intValue().as("commentCnt")
                        , resume.viewCnt
                        , resume.modifiedDate
                        , resume.isLock
                        , member.id.as("memberId")
                        , member.imageSrc
                        , member.nickname
                        , member.years
                ))
                .from(resumeScrap)
                .innerJoin(resume).on(resumeScrap.resumeId.eq(resume.id))
                .innerJoin(member).on(resume.memberId.eq(member.id))
                .leftJoin(resumeComment).on(resumeComment.resumeId.eq(resume.id))
                .leftJoin(resumeRecommend).on(resumeRecommend.resumeId.eq(resume.id))
                .groupBy(resume.id, resumeComment.id)
                .where(resume.isDelete.eq(true).and(resume.memberId.eq(memberId)))
                .fetch();
    }

    public Resume viewResume(Long resumeId) {
       return entityManager.createQuery("select r from Resume r " +
               "join fetch r.member " +
                               "where r.id = :resumeId"
               ,Resume.class)
               .setParameter("resumeId", resumeId)
               .getSingleResult();
    }

}
