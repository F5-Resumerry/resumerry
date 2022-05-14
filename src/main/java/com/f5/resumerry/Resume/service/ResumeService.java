package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.Resume.repository.ResumeRepository;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    public void uploadResume(Long id, String fullFileLink, UploadResumeDTO uploadResumeDTO) {
        String title = uploadResumeDTO.getTitle();
        String contents = uploadResumeDTO.getContents();
        CategoryEnum category = uploadResumeDTO.getCategory();
        Integer years = uploadResumeDTO.getYears();
        resumeRepository.uploadResume(id, fullFileLink, title, contents, category, years);
    }

    public void updateResume(Long memberId, Long resumeId, UploadResumeDTO uploadResumeDTO, String fullFileNamePath) {
        String title = uploadResumeDTO.getTitle();
        String contents = uploadResumeDTO.getContents();
        CategoryEnum category = uploadResumeDTO.getCategory();
        Integer years = uploadResumeDTO.getYears();
        resumeRepository.updateResume(memberId, resumeId, title, contents, category, years, fullFileNamePath);
    }

    public List<FilterViewResumeDTO> viewResumes(ResumeFilterDTO resumeFilterDTO, Long memberId) {
        return resumeRepository.examViewResumes(memberId);
    }
}
