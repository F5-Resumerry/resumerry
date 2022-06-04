package com.f5.resumerry.Resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumesFullResponse {
    private List<FilterViewResumeDTO> contents;
    private Integer totalPages;
}
