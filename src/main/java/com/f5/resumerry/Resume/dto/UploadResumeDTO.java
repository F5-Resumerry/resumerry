package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.selector.CategoryEnum;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadResumeDTO {
    private String title;
    private String contents;
    private CategoryEnum category;
    private Integer years;

//    @ApiParam("유저 토큰") @RequestHeader String token,
//    @ApiParam("이력서 제목") @RequestPart(required = false) String title,
//    @ApiParam("이력서 내용") @RequestPart(required = false) String contents,
//    @ApiParam("카테고리") @RequestPart(required = false) String category,
//    @ApiParam("years") @RequestPart(required = false) Integer years,
//    @ApiParam("파일") @RequestPart(value = "file",required = false)
//    MultipartFile file
//    private
}
