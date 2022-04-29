package com.f5.resumerry.Member.service;


import com.f5.resumerry.Member.domain.dto.MemberInfoDTO;
import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.entity.ConfirmationToken;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.domain.entity.MemberInfo;
import com.f5.resumerry.Member.repository.ConfirmationTokenRepository;
import com.f5.resumerry.Member.repository.MemberInfoRepository;
import com.f5.resumerry.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    @Transactional
    public Member saveMember(SignUpDTO memberDTO) {
//        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder().build();

        MemberInfo memberInfo = memberInfoDTO.toEntity();
        memberInfoRepository.save(memberInfo);

        memberDTO.setMemberInfo(memberInfo);
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
        return memberRepository.existsByAccountName(accountName);
    }

    @Override
    public Boolean checkExistsNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        Member member = memberRepository.findByAccountName(accountName);

        if (member == null) {
            log.error("Member not found in the database");
            throw new UsernameNotFoundException("Member not found in the database");
        }

        log.info("≈@@accountName : {}", accountName);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(member.getAccountName(), member.getPassword(), authorities);
    }


    public Optional<Member> findById(Long mbrNo)
    {
        Optional<Member> member = memberRepository.findById(mbrNo);
        return member;
    }

    @Transactional
    public void confirmEmail(String token) {
        ConfirmationToken findConfirmationToken = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(token);
        findConfirmationToken.useToken();	// expired 값 & certification 값을 true로 변경
    }

    public boolean checkEmail(String email) {
        List<ConfirmationToken> findConfirmationToken = confirmationTokenService.findAllByReceiverEmail(email);

        AtomicBoolean check = new AtomicBoolean(false);
        findConfirmationToken.stream().filter(s->s.isCertification()).forEach((s)->check.set(true));
        return check.get();
    }

    public boolean checkLogin(String accountName, String password){
        Member member = memberRepository.findByAccountName(accountName);
        AtomicBoolean check = new AtomicBoolean(false);
        if (member.getPassword() == password) {
            check.set(true);
        }
        return check.get();
    }

}