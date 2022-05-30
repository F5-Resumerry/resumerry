package com.f5.resumerry.Resume.dto;


import com.f5.resumerry.Resume.ResumeComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private Long memberId;
    private Long resumeId;
    private String isDelete;
    private Integer yPath;


    public ResumeComment toEntity(){
        ResumeComment build = ResumeComment.builder()
                .id(id)
                .contents(contents)
                .isAnonymous(isAnonymous)
                .resumeCommentGroup(resumeCommentGroup)
                .resumeCommentDepth(resumeCommentDepth)
                .memberId(memberId)
                .resumeId(resumeId)
                .isDelete(isDelete)
                .yPath(yPath)
                .build();
        return build;
    }


}
