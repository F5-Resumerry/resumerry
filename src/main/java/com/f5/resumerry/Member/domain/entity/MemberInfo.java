package com.f5.resumerry.Member.domain.entity;

import com.f5.resumerry.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        name = "member_info"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfo {

    @Id
    @GeneratedValue
    @Column(name = "member_info_id")
    private Long id;

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

    @Column(name = "image_src")
    private String imageSrc;

    @OneToOne(mappedBy = "memberInfo")
    private Member member;

}
