package com.f5.resumerry.Member.service;

import com.f5.resumerry.Member.domain.dto.ProfileDTO;
import com.f5.resumerry.Member.domain.dto.ScrapListDTO;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Resume.*;
import com.f5.resumerry.Resume.repository.ResumeRepository;
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

    @Transactional
    public List<ScrapListDTO> viewScrapInMyPage(String accountName) {
        List<ScrapListDTO> scrapListDTO = new ArrayList<ScrapListDTO>();
        Member member = memberRepository.findByAccountName(accountName);
        List<ResumeScrap> resumeList = member.getResumeScrapList();
        System.out.println(resumeList);
        for(ResumeScrap resumeScrap: resumeList){
            Optional<Resume> resumeOptional = resumeRepository.findById(resumeScrap.getResumeId());
            Resume resume = resumeOptional.orElse(null);
            if(!resume.getIsDelete()) {
                ScrapListDTO scrapListDTO1 = new ScrapListDTO();
                scrapListDTO1.setResumeId(resume.getId());
                scrapListDTO1.setTitle(resume.getTitle());
                scrapListDTO1.setCategory(resume.getCategory());
                scrapListDTO1.setFileLink(resume.getFileLink());
                scrapListDTO1.setViewCnt(resume.getViewCnt());
                scrapListDTO1.setYears(resume.getYears());
                scrapListDTO1.setMemberId(resume.getMemberId());
                scrapListDTO1.setModifiedDate(resume.getModifiedDate());
                scrapListDTO1.setImageSrc(resume.getMember().getImageSrc());
                scrapListDTO1.setNickname(resume.getMember().getNickname());
                scrapListDTO1.setRecommendCnt(resume.getResumeRecommendList().size());
                scrapListDTO1.setCommentCnt(resume.getResumeCommentList().size());

                List<String> hashtagList = new ArrayList<String>();
                for(ResumeHashtag resumeHashtag: resume.getResumeHashtagList()){
                    hashtagList.add(resumeHashtag.getHashtag().getHashtagName());
                }
                scrapListDTO1.setHashtagList(hashtagList);
                scrapListDTO.add(scrapListDTO1);
            }
        }
        return scrapListDTO;
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
