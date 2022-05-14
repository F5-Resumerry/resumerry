package com.f5.resumerry.Resume;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Post.entity.Post;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer viewCnt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @Column(name = "is_delete", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resume"))
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @OneToMany(mappedBy = "resume")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeComment> resumeCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeRecommend> resumeRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "resume")
    private List<ResumeScrap> resumeScrapList = new ArrayList<>();

//    @OneToMany(mappedBy = "resume")
//    private List<ResumeHashtag> resumeHashtagList = new ArrayList<>();
//    @Column(name = "hashtag")
//    private String hashtag;

}