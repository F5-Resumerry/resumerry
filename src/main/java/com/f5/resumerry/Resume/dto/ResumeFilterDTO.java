package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.selector.CategoryEnum;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@Builder
public class ResumeFilterDTO {

    @ApiParam("카테고리") private CategoryEnum category;
    @ApiParam("제목")  private String title;
    @ApiParam("시작 연도") private Integer startYear;
    @ApiParam("끝 연도")private Integer endYear;
    @ApiParam("해시태그") private String hashtag;
    @ApiParam("정렬 방법") private String sort;

    @ApiParam("페이지 번호") private Integer pageNo;

    public ResumeFilterDTO() {
        this.category = CategoryEnum.ALL;
        this.title = "";
        this.startYear = 0;
        this.endYear = 100;
        this.sort = "recent";
        this.pageNo = 0;
    }




}
