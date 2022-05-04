package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.dto.FindPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class PostCustomRepositoryImpl implements PostCustomRepository{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<FindPostDTO> findPosts(@Param("title") String title, @Param("category") String category, @Param("sort") String sort) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, size(p.postCommentList), p.views, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category) "
                        + "from Post p "
                        + "join p.member m "
                        + "join m.memberInfo mi "
                        + "join p.postCommentList pc "
                        + "group by p.id ", FindPostDTO.class)
                .getResultList();
    }
    public List<FindPostDTO> findPostsInMypage(Long memberId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, size(p.postCommentList), p.views, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category ) "
                        + "from Post p "
                        + "join p.member m "
                        + "where m.id in (:memberId) "
                        + "group by p.id ", FindPostDTO.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public FindPostDTO viewPost(Long memberId, Long postId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, size(p.postCommentList), p.views, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category ) "
                        + "from Post p "
                        + "join p.member m "
                        + "where m.id in (:memberId) and p.id in (:postId) "
                        + "group by p.id ", FindPostDTO.class)
                .setParameter("memberId", memberId)
                .setParameter("postId", postId)
                .getSingleResult();
    }

}
