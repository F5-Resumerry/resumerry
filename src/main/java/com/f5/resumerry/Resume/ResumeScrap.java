package com.f5.resumerry.Resume;

import com.f5.resumerry.Member.domain.entity.Member;
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
        name = "resume_scrap"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeScrap{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_scrap_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumescrap"))
    private Resume resume;

    @Column(name = "resume_id",insertable = false, updatable = false)
    private Long resumeId;
    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumescrap"))
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
