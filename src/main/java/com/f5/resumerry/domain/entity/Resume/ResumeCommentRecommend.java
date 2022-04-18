package com.f5.resumerry.domain.entity.Resume;

import com.f5.resumerry.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "resume_comment_recommend"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeCommentRecommend{

    @Id
    @GeneratedValue
    @Column(name = "resume_comment_recommend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_comment_id", foreignKey = @ForeignKey(name = "FK_resumecomment_resumecommentrecommend"))
    private ResumeComment resumeComment;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumecommentrecommend"))
    private Member member;
}
