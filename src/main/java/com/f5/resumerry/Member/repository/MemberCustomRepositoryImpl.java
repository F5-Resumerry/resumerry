package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.Member;

import com.f5.resumerry.Reward.TokenHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import static com.f5.resumerry.Reward.QTokenHistory.tokenHistory;
import static com.f5.resumerry.Member.domain.entity.QMemberInfo.memberInfo;

@Repository
@Transactional
public class MemberCustomRepositoryImpl implements  MemberCustomRepository{

//    @Autowired
//    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void updateMemberToken(Integer n, Long memberInfoId) {
//        entityManager.createQuery("update MemberInfo mi  set mi.token = mi.token - :n where mi.id = :memberInfoId")
//                .setParameter("n", n)
//                .setParameter("memberInfoId", memberInfoId)
//                .executeUpdate();
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
}
