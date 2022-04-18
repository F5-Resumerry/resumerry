package com.f5.resumerry.domain.entity.Resume;

import com.f5.resumerry.domain.entity.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "resume_recommend"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeRecommend{

    @Id
    @GeneratedValue
    @Column(name = "resume_recommend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumerecommend"))
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumerecommend"))
    private Member member;
}
