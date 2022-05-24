package com.f5.resumerry.Member.service;


import com.f5.resumerry.Member.domain.dto.AmendRequestDTO;
import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.selector.AwsUpload;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public interface MemberService {

    Member saveMember(SignUpDTO memberDTO);

    Member getMember(String accountName);

    Boolean checkExistsEmail(String email);
    Boolean checkExistsAccountName(String accountName);
    Boolean checkExistsNickname(String nickname);
    void amendMemberInfo(Long memberId, AmendRequestDTO amendRequestDTO, String fullImageSrc, MultipartFile file, String imageSrc, AwsUpload type);


}
