package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Resume.ResumeHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeHashtagRepository extends JpaRepository<ResumeHashtag, Long> {

}