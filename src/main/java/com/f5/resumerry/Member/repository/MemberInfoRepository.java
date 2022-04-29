package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {


}
