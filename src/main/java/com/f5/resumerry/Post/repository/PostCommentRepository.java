package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query("select max (pc.postCommentGroup) from PostComment pc where pc.postId = :postId")
    Integer findByPostId(Long postId);
}
