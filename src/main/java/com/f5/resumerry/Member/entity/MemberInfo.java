package com.f5.resumerry.Member.entity;

import com.f5.resumerry.converter.BooleanToYNConverter;

import javax.persistence.*;

@Entity
@Table(
        name = "member_info"
)
public class MemberInfo {

    @Id
    @GeneratedValue
    @Column(name = "member_info_id")
    private Long id;

    @Column(nullable = false)
    private Integer token;

    @Column(nullable = false)
    private Integer stack;

    @Column(name = "receive_advertisement")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean receiveAdvertisement;

    @Column(name = "email_verified")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean emailVerified;

    @Column(name = "image_src_s", nullable = false)
    private String imageSrcS;

    @Column(name = "image_src_m", nullable = false)
    private String imageSrcM;

    @Column(name = "image_src_l", nullable = false)
    private String imageSrcL;

    @OneToOne(mappedBy = "memberInfo")
    private Member member;
}
