package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.ResumeScrap;
import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.dto.ResumeRecommendDTO;
import com.f5.resumerry.Resume.dto.ResumeScrapDTO;
import com.f5.resumerry.Resume.dto.ViewResumeDTO;
import com.f5.resumerry.Resume.repository.ResumeRecommendRepository;
import com.f5.resumerry.Resume.repository.ResumeRepository;
import com.f5.resumerry.Resume.repository.ResumeScrapRepository;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    private final MemberRepository memberRepository;
    private final ResumeRecommendRepository resumeRecommendRepository;
    private final ResumeScrapRepository resumeScrapRepository;

    public List<ResumeDTO> viewResumesInMyPage(Long memberId) {
        return resumeRepository.viewResumesInMyPage(memberId);
    }

    public ViewResumeDTO viewResume(Long memberId, Long resumeId, Long tokenId) {
        resumeRepository.viewCnt(memberId, resumeId); // view cnt +
        // 소유자가 아닌경우 isOwner = false
        if (!memberId.equals(tokenId)) {
                // 스크랩 기록이 없는 경우  isScrap = false , isOwner = false
                if(!resumeRepository.existScrapByMemberIdAndResumeId(tokenId, resumeId))
                    return resumeRepository.noOwnerResumeAndNoScrap(tokenId, resumeId);
                // 스크랩이 있는경우 isScrap = true , isOwner = false
                return resumeRepository.noOwnerResumeCanScrap(tokenId, resumeId);

        } else {
            // 내꺼인 경우 isOwner = true isScrap = false
            return resumeRepository.viewResumeMine(tokenId,resumeId);
        }
    }

    public void uploadResume(Long id, String fullFileLink, String title, String contents, CategoryEnum category, Integer years) {
        resumeRepository.uploadResume(id, fullFileLink, title, contents, category, years);
    }

    public void deleteResume (Long memberId, Long postId) {
        //is_delete 컬럼 기본값 n update 형식으로 y로 수정
        resumeRepository.updateIsDelete(memberId, postId);
    }

    public boolean recommendResume(String accountName, Long resumeId){
        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        Member member = memberRepository.findByAccountName(accountName);
        Resume resume = resumeOptional.orElse(null);
        if (resumeRecommendRepository.existsByMemberAndResume(member, resume)){
            deleteResumeRecommend(resume, member);
        } else{
            saveResumeRecommend(resume, member);
        }
        return true;
    }

    @Transactional
    public void deleteResumeRecommend(Resume resume, Member member) {
        ResumeRecommend resumeRecommend = resumeRecommendRepository.findByResumeAndMember(resume, member);
        resumeRecommendRepository.deleteById(resumeRecommend.getId());
        return;
    }


    @Transactional
    public ResumeRecommend saveResumeRecommend(Resume resume, Member member) {
        ResumeRecommendDTO resumeRecommendDTO = new ResumeRecommendDTO();
        resumeRecommendDTO.setResume(resume);
        resumeRecommendDTO.setMember(member);
        ResumeRecommend resumeRecommend = resumeRecommendDTO.toEntity();
        return resumeRecommendRepository.save(resumeRecommend);
    }

    public boolean scrapResume(String accountName, Long resumeId){
        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        Member member = memberRepository.findByAccountName(accountName);
        Resume resume = resumeOptional.orElse(null);
        if (resumeScrapRepository.existsByMemberAndResume(member, resume)){
            deleteResumeScrap(resume, member);
        } else{
            saveResumeScrap(resume, member);
        }
        return true;
    }

    @Transactional
    public void deleteResumeScrap(Resume resume, Member member) {
        ResumeScrap resumeScrap = resumeScrapRepository.findByResumeAndMember(resume, member);
        resumeScrapRepository.deleteById(resumeScrap.getId());
        return;
    }


    @Transactional
    public ResumeScrap saveResumeScrap(Resume resume, Member member) {
        ResumeScrapDTO resumeScrapDTO = new ResumeScrapDTO();
        resumeScrapDTO.setResume(resume);
        resumeScrapDTO.setMember(member);
        ResumeScrap resumeScrap = resumeScrapDTO.toEntity();
        return resumeScrapRepository.save(resumeScrap);
    }
}
