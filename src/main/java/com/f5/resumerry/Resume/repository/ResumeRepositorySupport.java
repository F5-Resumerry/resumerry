package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.dto.FilterViewResumeDTO;
import com.f5.resumerry.Resume.dto.HashtagDTO;
import com.f5.resumerry.selector.CategoryEnum;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.f5.resumerry.Resume.QResume.resume;
import static com.f5.resumerry.Resume.QHashtag.hashtag;
import static com.f5.resumerry.Resume.QResumeHashtag.resumeHashtag;
import static com.f5.resumerry.Resume.QResumeComment.resumeComment;
import static com.f5.resumerry.Resume.QResumeRecommend.resumeRecommend;
import static com.f5.resumerry.Member.domain.entity.QMember.member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.logging.Filter;

@Repository
public class ResumeRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public ResumeRepositorySupport( EntityManager entityManager) {
        super(Resume.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    // member tabel의 내용을 다 끌어옴,,
    public PageImpl<FilterViewResumeDTO> findAllResumes(Pageable pageable, String title, Integer startYear, Integer endYear){
        JPQLQuery<FilterViewResumeDTO> query = jpaQueryFactory
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
                .where(resume.title.contains(title).and(resume.years.between(startYear, endYear)).and(resume.isDelete.eq(true)));

        Long totalCount = query.fetchCount();             // 2)
        List<FilterViewResumeDTO> results = querydsl().applyPagination(pageable, query).fetch();  // 3)
        return new PageImpl<>(results, pageable, totalCount);
    }

    public PageImpl<FilterViewResumeDTO> findCategoryResumes(Pageable pageable, String title, CategoryEnum category, Integer startYear, Integer endYear){
        JPQLQuery<FilterViewResumeDTO> query = jpaQueryFactory
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
                .where(resume.title.contains(title).and(resume.category.eq(category)).and(resume.years.between(startYear, endYear)).and(resume.isDelete.eq(true)));

        Long totalCount = query.fetchCount();             // 2)
        List<FilterViewResumeDTO> results = querydsl().applyPagination(pageable, query).fetch();  // 3)
        return new PageImpl<>(results, pageable, totalCount);
    }
    private Querydsl querydsl() {
        return Objects.requireNonNull(getQuerydsl());
    }
}
