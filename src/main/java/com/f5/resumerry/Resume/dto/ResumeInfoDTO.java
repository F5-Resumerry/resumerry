package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.selector.CategoryEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeInfoDTO {

    private Long userId;
    private Long resumeId;
}
