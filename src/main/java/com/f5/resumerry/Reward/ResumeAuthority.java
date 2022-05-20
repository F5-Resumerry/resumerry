package com.f5.resumerry.Reward;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "resume_authority"
)
public class ResumeAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_authority_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumeauthority"),insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumeauthority"), insertable = false, updatable = false)
    private Resume resume;

    @Column(name = "resume_id")
    private Long resumeId;
}
