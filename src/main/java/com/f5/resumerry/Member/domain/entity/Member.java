package com.f5.resumerry.Member.domain.entity;

import com.f5.resumerry.Order.Order;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
import com.f5.resumerry.Post.entity.PostCommentRecommend;
import com.f5.resumerry.Post.entity.PostCommentReport;
import com.f5.resumerry.Resume.*;
import com.f5.resumerry.Reward.TokenHistory;
import com.f5.resumerry.converter.BaseTimeEntity;
import com.f5.resumerry.converter.BooleanToYNConverter;
import com.f5.resumerry.selector.CategoryEnum;
import com.f5.resumerry.selector.Role;
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
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "account_name", name = "UK_account_name")
                , @UniqueConstraint(columnNames = "nickname", name = "UK_nickname")
                , @UniqueConstraint(columnNames = "email", name = "UK_email")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String introduce;

    @Column(nullable = false)
    private Integer years;

    @Column(name = "is_working", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isWorking;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_info_id", foreignKey = @ForeignKey(name = "FK_memberinfo_member"), insertable = false, updatable = false)
    private MemberInfo memberInfo;

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostComment> postCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostCommentRecommend> postCommentRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostCommentReport> postCommentReportList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Resume> resumeList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ResumeComment> resumeCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ResumeCommentRecommend> resumeCommentRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ResumeCommentReport> resumeCommentReportList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ResumeRecommend> resumeRecommendList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ResumeScrap> resumeScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<TokenHistory> tokenHistoryList = new ArrayList<>();

}
