package com.f5.resumerry.domain.entity.Post;

import com.f5.resumerry.domain.entity.Member;
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
    @JoinColumn(name = "post_comment_id", foreignKey = @ForeignKey(name = "FK_post_comment_post_comment_report"))
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_post_comment_report"))
    private Member member;
}
