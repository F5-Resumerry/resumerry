package com.f5.resumerry.Resume;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Resume resume;

    @Column(name = "resume_id")
    @JsonManagedReference
    private Long resumeId;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", foreignKey = @ForeignKey(name = "FK_hashtag_resumehashtag"), insertable = false, updatable = false)
    @JsonManagedReference
    private Hashtag hashtag;

    @Column(name = "hashtag_id")
    @JsonManagedReference
    private Long hashtagId;
}
