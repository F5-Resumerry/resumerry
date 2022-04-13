package com.f5.resumerry.domain.entity.Resume;

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
        name = "hashtag",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "hashtag_name", name = "UK_hashtag_name")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hashtag {
    @Id
    @GeneratedValue
    @Column(name = "hashtag")
    private Long id;

    @Column(name = "hashtag_name", nullable = false)
    private String hashtagName;

    @OneToMany(mappedBy = "hashtag")
    private List<ResumeHashtag> resumeHashtagList = new ArrayList<>();
}
