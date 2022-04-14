package com.f5.resumerry.domain.entity.Resume;

import com.f5.resumerry.domain.entity.Member;
import com.f5.resumerry.domain.entity.Post.Post;
import com.f5.resumerry.domain.entity.converter.BaseTimeEntity;
import com.f5.resumerry.domain.entity.selector.CategoryEnum;
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
        name = "resume",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "file_link", name = "UK_file_link")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resume extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String contents;

    @Column(nullable = false)
    private Integer years;

    @Column(name = "file_link", nullable = false)
    private String fileLink;

    @Column(nullable = false)
    private Integer views;

    @Column(nullable = false)
    private CategoryEnum category;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resume"))
    private Member member;

    @OneToMany(mappedBy = "resume")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeComment> resumeCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeRecommend> resumeRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeScrap> resumeScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeHashtag> resumeHashtagList = new ArrayList<>();

}