package com.f5.resumerry.Post.entity;

import com.f5.resumerry.Member.domain.entity.Member;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_comment_recommend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id", foreignKey = @ForeignKey(name = "FK_postcomment_postcommentrecommend"))
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_postcommentrecommend"))
    private Member member;
}
