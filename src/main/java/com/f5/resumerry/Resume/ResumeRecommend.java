package com.f5.resumerry.Resume;

import com.f5.resumerry.Member.domain.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_recommend_id")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumerecommend"), insertable = false, updatable = false)
    private Resume resume;

    @Column(name = "resume_id")
    private Long resumeId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumerecommend"), insertable = false, updatable = false)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
