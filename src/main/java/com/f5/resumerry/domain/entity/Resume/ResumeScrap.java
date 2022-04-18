package com.f5.resumerry.domain.entity.Resume;

import com.f5.resumerry.domain.entity.Member;
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
    @GeneratedValue
    @Column(name = "resume_scrap_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumescrap"))
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_resumescrap"))
    private Member member;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
