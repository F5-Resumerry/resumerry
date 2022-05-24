package com.f5.resumerry.Member.repository;


import com.f5.resumerry.Reward.TokenHistory;
import com.f5.resumerry.selector.CategoryEnum;

import java.util.List;

public interface MemberCustomRepository {

    void updateMemberToken(Integer n, Long memberId);

    List<TokenHistory> findAllTokenHistoryByUserId(Long userId);

    void amendMemberInfo(Long memberId, String nickname, Integer years, CategoryEnum category, String introduce, Boolean isWorking, String imageSrc);
}
