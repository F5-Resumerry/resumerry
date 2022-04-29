package com.f5.resumerry.Member.service;


import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.entity.Member;

public interface MemberService {

    Member saveMember(SignUpDTO memberDTO);

    Member getMember(String accountName);

    Boolean checkExistsEmail(String email);
    Boolean checkExistsAccountName(String accountName);
    Boolean checkExistsNickname(String nickname);
}
