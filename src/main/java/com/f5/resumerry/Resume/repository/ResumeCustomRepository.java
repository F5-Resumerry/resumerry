package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
import com.f5.resumerry.Resume.ResumeScrap;
import com.f5.resumerry.Resume.dto.FilterViewResumeDTO;
import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.dto.ResumeFilterDTO;
import com.f5.resumerry.Resume.dto.ViewResumeDTO;
import com.f5.resumerry.selector.CategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResumeCustomRepository {
    List<FilterViewResumeDTO> viewResumesInMyPage(Long memberId);

    List<FilterViewResumeDTO> viewScrapResumesInMyPage(Long memberId);
    Resume viewResume(Long resumeId);

}
