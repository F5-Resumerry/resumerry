package com.f5.resumerry.domain.entity.Post;

import com.f5.resumerry.domain.entity.Member;
import com.f5.resumerry.domain.entity.selector.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "post_category"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCategory{

    @Id
    @GeneratedValue
    @Column(name = "post_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_post_post_category"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_category_post_category"))
    private Category category;
}