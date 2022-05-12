package com.f5.resumerry.Resume.repository;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.dto.ResumeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeCustomRepository {


}
