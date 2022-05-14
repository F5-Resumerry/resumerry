package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumeFilterDTO {
    private CategoryEnum category;
    private String title;
    private Integer startYear;
    private Integer endYear;
    private String hashtag;
    private String sort;
}
