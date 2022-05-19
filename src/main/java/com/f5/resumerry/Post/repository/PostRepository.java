package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.selector.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository , JpaSpecificationExecutor<Post> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Post p "
            +"set p.title = :title, p.category = :category, p.contents = :contents, p.isAnonymous = :isAnonymous "
            +"where p.id = :postId and p.memberId = :memberId ")
    void updatePost(@Param("memberId") Long memberId, @Param("postId") Long postId, @Param("category") CategoryEnum category, @Param("contents") String contents, @Param("isAnonymous") Boolean isAnonymous, @Param("title") String title);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.viewCnt = p.viewCnt+1 where p.id = :postId and p.memberId = :memberId")
    void viewCnt(@Param("memberId") Long memberId, @Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.isDelete = 'Y' where p.id = :postId and p.memberId = :memberId")
    void updateIsDelete(@Param("memberId") Long memberId, @Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update PostComment pc set pc.isDelete = 'Y' where pc.memberId = :memberId and pc.id = :commentId")
    void updateCommentIsDelete(Long memberId, Long postId, Long commentId);





}
