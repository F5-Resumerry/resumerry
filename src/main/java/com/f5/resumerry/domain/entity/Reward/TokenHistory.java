package com.f5.resumerry.domain.entity.Reward;


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
        name = "token_history"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenHistory {

    @Id
    @GeneratedValue
    @Column(name = "token_history_id")
    private Long id;

    @Column(nullable = false)
    private Long count;

    @Column(nullable = false)
    private String reason;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_token_history"))
    private Member member;
}
