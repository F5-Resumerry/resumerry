package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
import com.f5.resumerry.Post.entity.PostCommentRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRecommendRepository extends JpaRepository<PostCommentRecommend, Long> {
    boolean existsByMemberIdAndPostCommentId(Long memberId, Long commentId);
}
