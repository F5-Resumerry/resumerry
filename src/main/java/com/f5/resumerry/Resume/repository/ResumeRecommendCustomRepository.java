package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.dto.ResumeRecommendDTO;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ResumeRecommendCustomRepository {

    List<ResumeRecommendDTO> findResumeRecommendByUserId(Long userId, Long resumeId);
}
