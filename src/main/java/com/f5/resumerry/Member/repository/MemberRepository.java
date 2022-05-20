package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Member findByAccountName(String accountName);

    Boolean existsByEmail(String email);
    Boolean existsByAccountName(String accountName);
    Boolean existsByNickname(String nickname);

    @Query("select m from Member m where m.email=?1")
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.id = ?1")
    Member findByMemberId(Long memberId);


}
