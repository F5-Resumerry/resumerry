package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.dto.ViewResumeDTO;
import com.f5.resumerry.Resume.repository.ResumeRepository;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

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
}
