package com.f5.resumerry.Resume;

import com.f5.resumerry.Member.entity.Member;
import com.f5.resumerry.converter.BaseTimeEntity;
import com.f5.resumerry.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(
        name = "resume_comment"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeComment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "resume_comment_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(name = "is_anonymous", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAnonymous;

    @Column(name = "resume_comment_group", nullable = false)
    private Integer resumeCommentGroup;

    @Column(name = "resume_comment_depth", nullable = false)
    private Integer resumeCommentDepth;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumecomment"))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumecomment"))
    private Resume resume;

    @OneToMany(mappedBy = "resumeComment")
    private List<ResumeCommentRecommend> resumeCommentRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "resumeComment")
    private List<ResumeCommentReport> resumeCommentReportList = new ArrayList<>();


}
