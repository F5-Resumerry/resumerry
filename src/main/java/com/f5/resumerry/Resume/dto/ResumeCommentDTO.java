package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Post.dto.PostChildCommentDTO;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeCommentDTO {
    //   is_anonymous  post_comment_depth post_comment_group  member_id  post_id  is_delete);
    private Long id;
    private String contents;
    private Boolean isAnonymous;
    private Integer resumeCommentGroup;
    private Integer resumeCommentDepth;
    private Member member;
    private Resume resume;
    private String isDelete;



    public ResumeComment toEntity(){
        ResumeComment build = ResumeComment.builder()
                .id(id)
                .contents(contents)
                .isAnonymous(isAnonymous)
                .resumeCommentGroup(resumeCommentGroup)
                .resumeCommentDepth(resumeCommentDepth)
                .member(member)
                .resume(resume)
                .isDelete(isDelete)
                .build();
        return build;
    }


}
