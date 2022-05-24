package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.Member;

import com.f5.resumerry.Reward.TokenHistory;
import com.f5.resumerry.selector.CategoryEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import static com.f5.resumerry.Reward.QTokenHistory.tokenHistory;
import static com.f5.resumerry.Member.domain.entity.QMemberInfo.memberInfo;
import static com.f5.resumerry.Member.domain.entity.QMember.member;

@Repository
public class MemberCustomRepositoryImpl implements  MemberCustomRepository{

    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void updateMemberToken(Integer n, Long memberInfoId) {

        queryFactory
                .update(memberInfo)
                .set(memberInfo.token, n)
                .where(memberInfo.id.eq(memberInfoId))
                .execute();
    }

    @Override
    public List<TokenHistory> findAllTokenHistoryByUserId(Long userId) {

        return queryFactory
                .selectFrom(tokenHistory)
                .where(tokenHistory.member.id.eq(userId))
                .fetch();
    }

    @Override
    @Transactional(readOnly = false)
    @Modifying
    public void amendMemberInfo(Long memberId, String nickname, Integer years, CategoryEnum category, String introduce, Boolean isWorking, String imageSrc) {

        queryFactory
                .update(member)
                .set(member.nickname, nickname)
                .set(member.years, years)
                .set(member.category, category)
                .set(member.introduce, introduce)
                .set(member.isWorking, isWorking)
                .set(member.imageSrc, imageSrc)
                .where(member.id.eq(memberId))
                .execute();
    }
}
