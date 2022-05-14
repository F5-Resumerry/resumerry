package com.f5.resumerry.Post.entity;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.converter.BaseTimeEntity;
import com.f5.resumerry.converter.BooleanToYNConverter;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

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
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    private Integer viewCnt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryEnum category;

    @Column(name = "is_anonymous", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAnonymous;

    @Column(name = "is_delete", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDelete;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_post"),insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(targetEntity = Resume.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_post"), insertable = false,  updatable = false)
    private Resume resume;

    @Column(name = "resume_id", insertable = false, updatable = false)
    private Long resumeId;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<PostComment> postCommentList = new ArrayList<>();

}