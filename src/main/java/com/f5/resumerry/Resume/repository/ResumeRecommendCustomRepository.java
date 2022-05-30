package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.dto.ResumeRecommendDTO;
import com.f5.resumerry.Resume.dto.ResumeSimilarRecommendDto;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ResumeRecommendCustomRepository {

    List<ResumeSimilarRecommendDto> findResumeRecommendByResumeId(Long userId, Long resumeId);
}
