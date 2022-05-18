package com.f5.resumerry.Resume;

import com.f5.resumerry.Member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_comment_recommend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_comment_id", foreignKey = @ForeignKey(name = "FK_RESUMECOMMENT_RESUMECOMMENTRECOMMEND"), insertable = false, updatable = false)
    private ResumeComment resumeComment;

    @Column(name = "resume_comment_id")
    private Long resumeCommentId;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_MEMBER_RESUMECOMMENTRECOMMEND"), insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;
}
