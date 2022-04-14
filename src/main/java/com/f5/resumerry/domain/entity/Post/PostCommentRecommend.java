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
        name = "post_comment_recommend"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCommentRecommend{

    @Id
    @GeneratedValue
    @Column(name = "post_comment_recommend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id", foreignKey = @ForeignKey(name = "FK_post_comment_post_comment_recommend"))
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_post_comment_recommend"))
    private Member member;
}
