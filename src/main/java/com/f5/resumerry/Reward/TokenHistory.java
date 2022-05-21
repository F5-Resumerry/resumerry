package com.f5.resumerry.Reward;


import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_history_id")
    private Long id;

    @Column(nullable = false)
    private Long tokenCnt;

    @Column(nullable = false)
    private String reason;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_tokenhistory"))
    private Member member;
}
