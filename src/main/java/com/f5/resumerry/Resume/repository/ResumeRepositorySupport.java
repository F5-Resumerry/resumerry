package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.dto.FilterViewResumeDTO;
import com.f5.resumerry.Resume.dto.ResumesDTO;
import com.f5.resumerry.selector.CategoryEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.f5.resumerry.Resume.QResume.resume;
import static com.f5.resumerry.Resume.QResumeHashtag.resumeHashtag;
import static com.f5.resumerry.Resume.QResumeComment.resumeComment;
import static com.f5.resumerry.Resume.QResumeRecommend.resumeRecommend;
import static com.f5.resumerry.Member.domain.entity.QMember.member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Filter;

@Repository
public class ResumeRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public ResumeRepositorySupport( EntityManager entityManager) {
        super(Resume.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public PageImpl<FilterViewResumeDTO> findAllResumes(Pageable pageable, String title, CategoryEnum category, Integer startYear, Integer endYear){
        JPQLQuery<FilterViewResumeDTO> query = jpaQueryFactory        // 1)
                .select(Projections.fields(FilterViewResumeDTO.class,
                        resume.id
                        ,resume.title
                        ,resume.contents
                        //,resumeRecommend.count().intValue()
                        ,resumeRecommend
                        ,resumeComment
                        ,resume.viewCnt
                        ,resume.modifiedDate
                        ,resume.resumeHashtagList
                        ,resume.isLock
                        ,resume.memberId
                        ,resume.member.imageSrc
                        ,resume.member.nickname
                        ,resume.member.years
                ))
                .from(resume)
                .innerJoin(member).on(member.id.eq(resume.memberId))
                .innerJoin(resumeHashtag).on(resume.id.eq(resumeHashtag.resumeId))
                .innerJoin(resumeComment).on(resume.id.eq(resumeComment.resumeId))
                .innerJoin(resumeRecommend).on(resume.id.eq(resumeRecommend.resumeId))
                .where(resume.title.contains(title).and(resume.category.eq(category)).and(resume.years.between(startYear, endYear)));

        Long totalCount = query.fetchCount();             // 2)
        List<FilterViewResumeDTO> results = getQuerydsl().applyPagination(pageable, query).fetch();  // 3)
        return new PageImpl<>(results, pageable, totalCount);
    }
}
