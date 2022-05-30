package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByHashtagName(String hashtagName);
    Boolean existsByHashtagName(String hashtagName);
    @Transactional
    @Query("select h from Hashtag h where h.id = ?1")
    Hashtag findByHashtagId(Long id);
}