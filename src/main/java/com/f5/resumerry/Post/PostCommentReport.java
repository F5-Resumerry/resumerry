package com.f5.resumerry.Post;

import com.f5.resumerry.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "post_comment_report"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCommentReport{

    @Id
    @GeneratedValue
    @Column(name = "post_comment_report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id", foreignKey = @ForeignKey(name = "FK_postcomment_postcommentreport"))
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_postcommentreport"))
    private Member member;
}
