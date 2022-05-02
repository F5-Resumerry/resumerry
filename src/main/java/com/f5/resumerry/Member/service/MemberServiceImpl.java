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

import org.apache.catalina.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private static MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private static SaltUtil saltUtil;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public Member saveMember(SignUpDTO memberDTO) {

        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
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

//    @Override
//    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
//        Member member = memberRepository.findByAccountName(accountName);
//
//        if (member == null) {
//            log.error("Member not found in the database");
//            throw new UsernameNotFoundException("Member not found in the database");
//        }
//
//        log.info("≈@@accountName : {}", accountName);
//
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        return new org.springframework.security.core.userdetails.User(member.getAccountName(), member.getPassword(), authorities);
//    }


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

//    public boolean checkLogin(String accountName, String password){
//
//        String encodePassword = passwordEncoder.encode(password);
//        Member member = memberRepository.findByAccountName(accountName);
//        log.info("checkLogin\n");
//        log.info(member.getPassword());
//        log.info("\n");
//        log.info(encodePassword);
//        log.info("\n");
//        AtomicBoolean check = new AtomicBoolean(false);
//        if (member.getPassword().equals(encodePassword)) {
//            check.set(true);
//        }
//        return check.get();
//    }

    public static Member checkLogin(String id, String pw) throws Exception{
        Member member = memberRepository.findByAccountName(id);
        if(member==null) throw new Exception ("멤버가 조회되지 않음");
        String salt = member.getSalt();
        pw = saltUtil.encodePassword(salt,pw);
        if(!member.getPassword().equals(pw))
            throw new Exception ("비밀번호가 틀립니다.");
        return member;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
