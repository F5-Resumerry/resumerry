package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    @Transactional
    @Query("select mi.token from MemberInfo mi where mi.member.id = ?1")
    Integer findByMemberId(Long id);

}
