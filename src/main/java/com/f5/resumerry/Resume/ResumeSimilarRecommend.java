package com.f5.resumerry.Resume;

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
        name = "resume_similar_recommend"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeSimilarRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_similar_recommend_id")
    private Long id;

    @Column(name = "source_resume_id")
    private Long sourceResumeId;

    @Column(name = "similar_resume_id")
    private Long similarResumeId;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
