package com.f5.resumerry.Post;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Member.Member;
import com.f5.resumerry.converter.BaseTimeEntity;
import com.f5.resumerry.converter.BooleanToYNConverter;
import com.f5.resumerry.selector.CategoryEnum;
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
        name = "post"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Integer views;

    @Column(nullable = false)
    private CategoryEnum category;

    @Column(name = "is_anonymous", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAnonymous;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_post"))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_post"))
    private Resume resume;

    @OneToMany(mappedBy = "post")
    private List<PostComment> postCommentList = new ArrayList<>();

}