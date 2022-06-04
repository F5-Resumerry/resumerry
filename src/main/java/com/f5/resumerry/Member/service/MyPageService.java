package com.f5.resumerry.Member.service;

import com.f5.resumerry.Member.domain.dto.ProfileDTO;
import com.f5.resumerry.Member.domain.dto.ScrapListDTO;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Resume.*;
import com.f5.resumerry.Resume.dto.FilterViewResumeDTO;
import com.f5.resumerry.Resume.repository.ResumeHashtagRepository;
import com.f5.resumerry.Resume.repository.ResumeRepository;
import com.f5.resumerry.security.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MyPageService {
    private final MemberRepository memberRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeHashtagRepository resumeHashtagRepository;

    @Transactional
    public List<FilterViewResumeDTO> viewScrapInMyPage(Long memberId) {

        List<FilterViewResumeDTO> lists = resumeRepository.viewScrapResumesInMyPage(memberId);
        for(FilterViewResumeDTO list : lists) {
            List<String> hashtagLists = new ArrayList<String>();
            Long resumeId = list.getResumeId();
            // resume hash tag 에서 list 반환
            for(ResumeHashtag resumeHashtag : resumeHashtagRepository.findByResumeId(resumeId)) {
                Long hashtagId = resumeHashtag.getHashtagId();
                hashtagLists.add(resumeHashtag.getHashtag().getHashtagName());
            }
            list.setHashtag(hashtagLists);
        }
        return lists;
    }
    @Transactional
    public ProfileDTO viewProfileInMyPage(String accountName) {
        ProfileDTO profileDTO = new ProfileDTO();
        Member member = memberRepository.findByAccountName(accountName);

        profileDTO.setMemberId(member.getId());
        profileDTO.setAccountName(accountName);
        profileDTO.setNickname(member.getNickname());
        profileDTO.setYears(member.getYears());
        profileDTO.setCategory(member.getCategory());
        profileDTO.setEmail(member.getEmail());
        profileDTO.setIntroduce(member.getIntroduce());
        profileDTO.setRole(member.getRole());
        profileDTO.setImageSrc(member.getImageSrc());
        profileDTO.setStack(member.getMemberInfo().getStack());
        profileDTO.setToken(member.getMemberInfo().getToken());
        return profileDTO;
    }


}
