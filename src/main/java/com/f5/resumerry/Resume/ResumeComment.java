package com.f5.resumerry.Resume;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.converter.BaseTimeEntity;
import com.f5.resumerry.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumecomment"), insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumecomment"), insertable = false, updatable = false)
    private Resume resume;

    @Column(name = "resume_id")
    private Long resumeId;

    @OneToMany(mappedBy = "resumeComment")
    private List<ResumeCommentRecommend> resumeCommentRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "resumeComment")
    private List<ResumeCommentReport> resumeCommentReportList = new ArrayList<>();

    @Column(name = "is_delete")
    private String isDelete;

    @Column(name = "y_path")
    private Integer yPath;
}
