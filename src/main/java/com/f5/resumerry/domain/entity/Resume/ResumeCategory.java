package com.f5.resumerry.domain.entity.Resume;


import com.f5.resumerry.domain.entity.Member;
import com.f5.resumerry.domain.entity.Post.Post;
import com.f5.resumerry.domain.entity.selector.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "resume_category"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeCategory{

    @Id
    @GeneratedValue
    @Column(name = "resume_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "FK_resume_resume_category"))
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_category_resume_category"))
    private Category category;
}