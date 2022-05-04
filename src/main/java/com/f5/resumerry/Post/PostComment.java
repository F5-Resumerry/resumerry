package com.f5.resumerry.Post;

import com.f5.resumerry.Member.domain.entity.Member;
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
        name = "post_comment"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_comment_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(name = "is_anonymous", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAnonymous;

    @Column(name = "post_comment_group", nullable = false)
    private Integer postCommentGroup;

    @Column(name = "post_comment_depth", nullable = false)
    private Integer postCommentDepth;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_postcomment"))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_post_postcomment"))
    private Post post;

    @OneToMany(mappedBy = "postComment")
    private List<PostCommentRecommend> postCommentRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "postComment")
    private List<PostCommentReport> postCommentReportList = new ArrayList<>();

}
