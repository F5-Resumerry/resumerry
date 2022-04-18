package com.f5.resumerry.domain.entity.Member;

import com.f5.resumerry.domain.entity.selector.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
        name = "member_category"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCategory{

    @Id
    @GeneratedValue
    @Column(name = "member_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_membercategory"))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_category_membercategory"))
    private Category category;
}