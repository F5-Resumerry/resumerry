package com.f5.resumerry.Resume;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_hashtag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resumehashtag"), insertable = false, updatable = false)
    private Resume resume;

    @Column(name = "resume_id")
    private Long resumeId;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", foreignKey = @ForeignKey(name = "FK_hashtag_resumehashtag"), insertable = false, updatable = false)
    private Hashtag hashtag;

    @Column(name = "hashtag_id")
    private Long hashtagId;
}
