package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.DocFlavor;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByHashtagName(String hashtagName);
    Boolean existsByHashtagName(String hashtagName);
}