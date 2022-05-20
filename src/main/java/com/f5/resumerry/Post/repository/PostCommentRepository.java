package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);

    Optional<PostComment> findById(Long postCommentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update MemberInfo mi set mi.stack = mi.stack + 5, mi.token = mi.token + 1 where mi.id in (select m.memberInfoId from Member m where m.id = :memberId)")
    void updateCommentUploadReward(Long memberId);
}
