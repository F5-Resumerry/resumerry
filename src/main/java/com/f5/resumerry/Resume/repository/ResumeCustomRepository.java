package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
import com.f5.resumerry.Resume.ResumeScrap;
import com.f5.resumerry.Resume.dto.FilterViewResumeDTO;
import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.dto.ResumeFilterDTO;
import com.f5.resumerry.Resume.dto.ViewResumeDTO;
import com.f5.resumerry.selector.CategoryEnum;

import java.util.List;

public interface ResumeCustomRepository {
    List<ResumeDTO> viewResumesInMyPage(Long memberId);

    void uploadResume(Long id, String fullFileLink, String title, String contents, CategoryEnum category, Integer years);

   // void updateResume(Long memberId, Long resumeId, String title, String contents, CategoryEnum category, Integer years, String fullFileNamePath);

    List<Resume> findAllWithMember(String t, Integer sy, Integer ey, CategoryEnum c);
    List<Resume> findAllWithMemberByYears(String t, Integer sy, Integer ey, CategoryEnum c);
    List<Resume> findAllWithMemberByRecommend(String t, Integer sy, Integer ey, CategoryEnum c);
    List<Resume> findAllWithMemberByView(String t, Integer sy, Integer ey, CategoryEnum c);
    List<ResumeHashtag> findHashtag(Long resumeId);


    Resume viewResume(Long resumeId);
}
