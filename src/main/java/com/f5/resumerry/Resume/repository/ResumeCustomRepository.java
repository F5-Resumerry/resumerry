package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.dto.ViewResumeDTO;
import com.f5.resumerry.selector.CategoryEnum;

import java.util.List;

public interface ResumeCustomRepository {
    List<ResumeDTO> viewResumesInMyPage(Long memberId);



    // 내꺼인 경우, isSrap = false , isOwner = true
    ViewResumeDTO viewResumeMine(Long tokenId, Long resumeId);

    ViewResumeDTO noOwnerResumeAndNoScrap(Long tokenId, Long resumeId);

    ViewResumeDTO noOwnerResumeCanScrap(Long tokenId, Long resumeId);

    void uploadResume(Long id, String fullFileLink, String title, String contents, CategoryEnum category, Integer years);

    // 이 멤버가 해당 이력서를 스크랩 했는가

}
