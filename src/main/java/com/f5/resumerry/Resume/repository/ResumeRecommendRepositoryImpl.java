package com.f5.resumerry.Resume.repository;


import com.f5.resumerry.Resume.dto.ResumeRecommendDTO;
import com.f5.resumerry.Resume.dto.ResumeSimilarRecommendDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.f5.resumerry.Resume.QResume.resume;
import static com.f5.resumerry.Resume.QResumeSimilarRecommend.resumeSimilarRecommend;

public class ResumeRecommendRepositoryImpl implements ResumeRecommendCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ResumeRecommendRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ResumeSimilarRecommendDto> findResumeRecommendByResumeId(Long userId, Long resumeId) {

        return queryFactory
                .select(Projections.constructor(ResumeSimilarRecommendDto.class,
                        resume.id
                        , resume.viewCnt
                        , resume.title
                        , resume.fileLink
                        , resume.contents
                        , resume.createdDate
                ))
                .from(resumeSimilarRecommend)
                .innerJoin(resume).on(resume.id.eq(resumeSimilarRecommend.similarResumeId)
                        .and(resumeSimilarRecommend.sourceResumeId.eq(resumeId))
                )
                .orderBy(resumeSimilarRecommend.createdDate.desc())
                .limit(5)
                .fetch();
    }

}
