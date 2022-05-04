package com.f5.resumerry.selector;

import com.f5.resumerry.Member.domain.entity.MemberCategory;
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
        name = "category",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "category", name = "UK_category")
    }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private CategoryEnum category;

    @OneToMany(mappedBy = "category")
    private List<MemberCategory> memberCategoryList = new ArrayList<>();
}
