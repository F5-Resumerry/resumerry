package com.f5.resumerry.Member.domain.entity;

import com.f5.resumerry.converter.BooleanToYNConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "member_info"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_info_id")
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private Integer token = 0 ;

    @Column(nullable = false)
    private Integer stack = 0;

    @Column(name = "receive_advertisement")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean receiveAdvertisement;

    @Column(name = "email_verified")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean emailVerified = true;

    @OneToOne(mappedBy = "memberInfo")
    @JsonBackReference
    private Member member;


}
