package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.repository.ResumeRepository;
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
}
