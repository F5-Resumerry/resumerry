package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByAccountName(String accountName);

    Boolean existsByEmail(String email);
    Boolean existsByAccountName(String accountName);
    Boolean existsByNickname(String nickname);


}
