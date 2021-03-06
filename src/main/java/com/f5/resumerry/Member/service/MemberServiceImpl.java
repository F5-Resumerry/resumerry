package com.f5.resumerry.Member.service;


import com.f5.resumerry.Member.domain.dto.AmendRequestDTO;
import com.f5.resumerry.Member.domain.dto.MemberInfoDTO;
import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.dto.SignUpReqDTO;
import com.f5.resumerry.Member.domain.entity.ConfirmationToken;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.domain.entity.MemberInfo;
import com.f5.resumerry.Member.repository.MemberInfoRepository;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Reward.TokenHistory;
import com.f5.resumerry.aws.AwsS3Service;
import com.f5.resumerry.selector.AwsUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final SaltUtil saltUtil;

    private final AwsS3Service awsS3Service;

    public List<TokenHistory> getAllTokenHistory(Long userId) {
        return memberRepository.findAllTokenHistoryByUserId(userId);
    }

    @Override
    @Transactional
    public Member saveMember(SignUpReqDTO memberReqDTO) {
        String salt = saltUtil.genSalt();
        SignUpDTO memberDTO = new SignUpDTO(memberReqDTO);
        memberDTO.setSalt(salt);
        memberDTO.setPassword(saltUtil.encodePassword(salt, memberDTO.getPassword()));
        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder().build();

        MemberInfo memberInfo = memberInfoDTO.toEntity();
        memberInfoRepository.save(memberInfo);

        memberDTO.setMemberInfo(memberInfo);
        memberDTO.setMemberInfoId(memberInfo.getId());

        Member member = memberDTO.toEntity();
        return memberRepository.save(member);
    }

    @Override
    public Member getMember(String accountName) {
        return memberRepository.findByAccountName(accountName);
    }

    @Override
    public Boolean checkExistsEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public Boolean checkExistsAccountName(String accountName) {
        return !memberRepository.existsByAccountName(accountName);
    }

    @Override
    public Boolean checkExistsNickname(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public void confirmEmail(String token) {
        ConfirmationToken findConfirmationToken = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(token);
        findConfirmationToken.useToken();	// expired ??? & certification ?????? true??? ??????
    }

    public boolean checkEmail(String email) {
        List<ConfirmationToken> findConfirmationToken = confirmationTokenService.findAllByReceiverEmail(email);

        AtomicBoolean check = new AtomicBoolean(false);
        findConfirmationToken.stream().filter(s->s.isCertification()).forEach((s)->check.set(true));
        return check.get();
    }


    public boolean checkLogin(String accountName, String password)  {

        Member member = memberRepository.findByAccountName(accountName);
        AtomicBoolean check = new AtomicBoolean(false);
        if(member==null) {
            //throw new Exception ("????????? ???????????? ??????");
            return check.get();
        }
        String salt = member.getSalt();
        String encodePassword = saltUtil.encodePassword(salt,password);


        if (member.getPassword().equals(encodePassword)) {
            check.set(true);
        }
        return check.get();
    }

    public void amendMemberInfo(Long memberId, AmendRequestDTO amendRequestDTO, String fullImageSrc, MultipartFile file, String imageSrc, AwsUpload type){
        try {
            awsS3Service.upload(file, imageSrc, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(memberId);
        memberRepository.amendMemberInfo(memberId, amendRequestDTO.getNickname(), amendRequestDTO.getYears(), amendRequestDTO.getCategory() ,amendRequestDTO.getIntroduce(),amendRequestDTO.getIsWorking(), fullImageSrc);
    }

    public Long findMemberId(String accountName){
        Member member = memberRepository.findByAccountName(accountName);
        return member.getId();
    }

}
