package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.dto.ResumeDTO;

import java.util.List;

public interface ResumeCustomRepository {
    List<ResumeDTO> viewResumesInMyPage(Long memberId);
}
