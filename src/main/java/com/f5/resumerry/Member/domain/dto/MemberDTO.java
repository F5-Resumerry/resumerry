package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.selector.CategoryEnum;
import com.f5.resumerry.selector.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;




@Data
public class MemberDTO {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String accountName;
    private String email;
    private String introduce;
    private Boolean isWorking;
    private String nickname;
    private Role role;
    private Integer years;
    private CategoryEnum category;
    private String imageSrc;



    public MemberDTO(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String accountName, String email, String introduce, Boolean isWorking, String nickname, Role role, Integer years, CategoryEnum category, String imageSrc) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.accountName = accountName;
        this.email = email;
        this.introduce = introduce;
        this.isWorking = isWorking;
        this.nickname = nickname;
        this.role = role;
        this.years = years;
        this.category = category;
        this.imageSrc = imageSrc;
    }

    public static MemberDTO of(Member member){
        return new MemberDTO(
                member.getId(),
                member.getCreatedDate(),
                member.getModifiedDate(),
                member.getAccountName(),
                member.getEmail(),
                member.getIntroduce(),
                member.getIsWorking(),
                member.getNickname(),
                member.getRole(),
                member.getYears(),
                member.getCategory(),
                member.getImageSrc()
        );
    }
    public static MemberDTO InfoOf(String nickname, Integer years, CategoryEnum category, String introduce, Boolean isWorking, String imageSrc, Member member){
        LocalDateTime localDateTime = LocalDateTime.now();
        return new MemberDTO(
                member.getId(),
                member.getCreatedDate(),
                localDateTime,
                member.getAccountName(),
                member.getEmail(),
                introduce,
                isWorking,
                nickname,
                member.getRole(),
                years,
                category,
                imageSrc
        );
    }

    public Member toEntity(Long memberId,String nickname, Integer years, CategoryEnum category, String introduce, Boolean isWorking, String imageSrc) {
        return Member.builder()
                .id(memberId)
                .nickname(nickname)
                .years(years)
                .category(category)
                .introduce(introduce)
                .isWorking(isWorking)
                .imageSrc(imageSrc).build();
    }

}
