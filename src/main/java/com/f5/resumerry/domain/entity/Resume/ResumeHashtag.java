package com.f5.resumerry.domain.entity.Resume;

import com.f5.resumerry.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "resume_hashtag"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeHashtag{

    @Id
    @GeneratedValue
    @Column(name = "resume_hashtag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumehashtag"))
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", foreignKey = @ForeignKey(name = "FK_hashtag_resumehashtag"))
    private Hashtag hashtag;
}
