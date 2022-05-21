package com.f5.resumerry.Member.repository;


import com.f5.resumerry.Reward.TokenHistory;

import java.util.List;

public interface MemberCustomRepository {

    void updateMemberToken(Integer n, Long memberId);

    List<TokenHistory> findAllTokenHistoryByUserId(Long userId);
}
