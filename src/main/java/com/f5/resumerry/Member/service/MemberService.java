package com.f5.resumerry.Member.service;


import com.f5.resumerry.Member.dto.SignUpDTO;
import com.f5.resumerry.Member.entity.Member;

public interface MemberService {

    Member saveMember(SignUpDTO memberDTO);

    Member getMember(String accountName);

    Boolean checkExistsEmail(String email);
    Boolean checkExistsAccountName(String accountName);
    Boolean checkExistsNickname(String nickname);
}
