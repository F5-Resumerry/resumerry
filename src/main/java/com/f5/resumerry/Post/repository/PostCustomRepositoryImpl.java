package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.selector.CategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class PostCustomRepositoryImpl implements PostCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PostsDTO> findPosts(@Param("title") String title, @Param("category") String category) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostsDTO(p.id, p.title, p.contents, p.postCommentList.size, p.viewCnt, p.isAnonymous, m.imageSrc , p.memberId, m.nickname, p.modifiedDate, p.category) "
                        + "from Post p inner join p.member m "
                + "where p.title like concat('%', :title, '%') and p.isDelete = true "
                + "order by p.createdDate desc "
                        , PostsDTO.class)
                .setParameter("title", title)
                .setMaxResults(10)
                .getResultList();
    }
    public List<PostsDTO> findPostsNotAll(String title, String category) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostsDTO(p.id, p.title, p.contents, p.postCommentList.size, p.viewCnt, p.isAnonymous, m.imageSrc , p.memberId, m.nickname, p.modifiedDate, p.category) "
                                + "from Post p inner join p.member m "
                                + "where p.category = :category "
                                + "and p.title like concat('%', :title, '%') and p.isDelete = true "
                                + "order by p.createdDate desc "
                        , PostsDTO.class)
                .setParameter("category", CategoryEnum.valueOf(category))
                .setParameter("title", title)
                .setMaxResults(10)
                .getResultList();
    }
    public List<PostsDTO> findPostsView(String title, String category) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostsDTO(p.id, p.title, p.contents, p.postCommentList.size, p.viewCnt, p.isAnonymous, m.imageSrc , p.memberId, m.nickname, p.modifiedDate, p.category) "
                                + "from Post p left join p.member m "
                                + "where p.title like concat('%', :title, '%') and p.isDelete = true "
                                + "order by p.viewCnt desc "
                        , PostsDTO.class)
                .setParameter("title", title)
                .setMaxResults(10)
                .getResultList();
    }

    public List<PostsDTO> findPostsViewNotAll(String title, String category){
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostsDTO(p.id, p.title, p.contents, p.postCommentList.size, p.viewCnt, p.isAnonymous, m.imageSrc , p.memberId, m.nickname, p.modifiedDate, p.category) "
                                + "from Post p left join p.member m "
                                + "where p.category = :category "
                                + "and p.title like concat('%', :title, '%') and p.isDelete = true  "
                                + "order by p.viewCnt desc "
                        , PostsDTO.class)
                .setParameter("category", CategoryEnum.valueOf(category))
                .setParameter("title", title)
                .setMaxResults(10)
                .getResultList();
    }

    public List<PostsDTO> findPostsInMyPage(Long memberId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostsDTO(p.id, p.title, p.contents, size(p.postCommentList), p.viewCnt, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category ) "
                        + "from Post p, Member m "
                        //+ "join fetch p.member m "
                        + "where m.id in (:memberId) "
                        + "group by p.id ", PostsDTO.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public FindPostDTO viewPost(Long memberId, Long postId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, size(p.postCommentList), p.viewCnt, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category, true) "
                        + "from Post p "
                        + "join p.member m "
                        + "where m.id in (:memberId) and p.id in (:postId) and p.isDelete = true "
                        + "group by p.id ", FindPostDTO.class)
                .setParameter("memberId", memberId)
                .setParameter("postId", postId)
                .getSingleResult();
    }

    public FindPostDTO viewNotOwnPost(Long postId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, size(p.postCommentList), p.viewCnt, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category, false) "
                        + "from Post p "
                        + "join p.member m "
                        + "where p.id in (:postId) and p.isDelete = true "
                        + "group by p.id ", FindPostDTO.class)
                .setParameter("postId", postId)
                .getSingleResult();
    }

    @Transactional
    public void registerPost(RegisterPostDTO r) {
        String anonymous = r.getIsAnonymous() ? "Y" : "N";
        entityManager.createNativeQuery("insert into post (title, category, contents, is_anonymous, viewCnt, member_id, resume_id) values (?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, r.getTitle() )
                .setParameter(2, String.valueOf(r.getCategory()))
                .setParameter(3, r.getContents())
                .setParameter(4, anonymous)
                .setParameter(5, r.getViewCnt())
                .setParameter(6, r.getMemberId())
                .setParameter(7, r.getResumeId())
                .executeUpdate();
    }

    // 댓글
    @Transactional
    public void registerPostComment(PostCommentDTO pc) {
        String anonymous = pc.getIsAnonymous() ? "Y" : "N";
        entityManager.createNativeQuery("insert into post_comment (contents, is_anonymous, post_comment_depth, post_comment_group, member_id, post_id) values (?, ?, ?, ?, ?, ?)")
                .setParameter(1, pc.getContents())
                .setParameter(2, anonymous)
                .setParameter(3, pc.getPostCommentDepth())
                .setParameter(4, pc.getPostCommentGroup())
                .setParameter(5, pc.getMemberId())
                .setParameter(6, pc.getPostId())
                .executeUpdate();
    }

//    public List<PostParentCommentDTO> findComments(Long postId, List<PostChildCommentDTO> pcc) {
//        Object PostChildCommentDTO = pcc;
//        return entityManager.createQuery(" select new com.f5.resumerry.Post.dto.PostParentCommentDTO(pc.id, pc.memberId, m.imageSrc, m.nickname, pc.contents, pc.postCommentRecommendList.size, pc.postCommentReportList.size, pc.isAnonymous, true, pc.modifiedDate, pc.postCommentGroup,pc.postCommentDepth, pc.postChildComments)"
//                + "from PostComment pc join pc.member m "
//                        +"where pc.postCommentDepth = 0 ", PostParentCommentDTO.class)
//                .setParameterList(List<PostChildCommentDTO>, )
//                .getResultList();
//    }
    public  List<PostChildCommentDTO> findChildComments(Integer groupNum, Long postId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostChildCommentDTO(pc.id, m.id, m.imageSrc, m.imageSrc, pc.contents, pc.postCommentRecommendList.size, pc.postCommentReportList.size, pc.isAnonymous, true , pc.modifiedDate, 1)"
                + "from PostComment pc join pc.member m "
                        + "where pc.postCommentGroup = :groupNum and pc.postCommentDepth = 1", PostChildCommentDTO.class)
                .setParameter("groupNum", groupNum)
                .getResultList();
    }


    public void registerRecommendComment(PostCommentRecommendDTO pcr) {
        entityManager.createNativeQuery("insert into post_comment_recommend (member_id, post_comment_id, post_id) values (?, ?, ?)")
                .setParameter(1, pcr.getMemberId())
                .setParameter(2, pcr.getCommentId())
                .setParameter(3, pcr.getPostId())
                .executeUpdate();
    }

    public void banComment( Long postId, Long commentId, Long reportMember) {
        entityManager.createNativeQuery("insert into post_comment_report (member_id, post_comment_id, post_id) values (?, ?, ?)")
                .setParameter(1, reportMember)
                .setParameter(2, commentId)
                .setParameter(3, postId)
                .executeUpdate();
    }




}
