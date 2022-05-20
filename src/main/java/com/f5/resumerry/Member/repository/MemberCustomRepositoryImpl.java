package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class MemberCustomRepositoryImpl implements  MemberCustomRepository{

    @Autowired
    private EntityManager entityManager;

    public void updateMemberToken(Integer n, Long memberInfoId) {
        entityManager.createQuery("update MemberInfo mi  set mi.token = mi.token - :n where mi.id = :memberInfoId")
                .setParameter("n", n)
                .setParameter("memberInfoId", memberInfoId)
                .executeUpdate();
    }



}
