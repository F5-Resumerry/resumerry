package com.f5.resumerry.Resume.repository;


import com.f5.resumerry.Resume.dto.ResumeRecommendDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.f5.resumerry.Resume.QResumeRecommend.resumeRecommend;
import static com.f5.resumerry.Resume.QResume.resume;

public class ResumeRecommendRepositoryImpl implements ResumeRecommendCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ResumeRecommendRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ResumeRecommendDTO> findResumeRecommendByUserId(Long userId, Long resumeId) {

        return queryFactory
                .select(Projections.constructor(ResumeRecommendDTO.class,
                        resumeRecommend.resumeId
                        , resume.title
                        , resume.fileLink
                        , resume.contents
                        , resume.createdDate
                ))
                .from(resumeRecommend)
                .innerJoin(resume).on(resume.id.eq(resumeRecommend.resumeId))
                .where(
                        resumeRecommend.memberId.eq(userId)
                )
                .orderBy(resumeRecommend.createdDate.desc())
                .limit(5)
                .fetch();
    }
}
