package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.dto.*;
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

//    public List<PostDTO> viewPosts(PostDTO p) {
//        String anonymous = p.getIsAnonymous() == true ? "Y" : "N";
//        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostDTO(p.id, p.title, p.contents, p.views, p.category, p.a , p.isDelete, p.isDelete, p.memberId, p.resumeId )"
//                + "from Post p",PostDTO.class)
//                .getResultList();
//    }
    @Override
    public List<FindPostDTO> findPosts(@Param("title") String title, @Param("category") String category, @Param("sort") String sort) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, p.postCommentList.size, p.views, p.isAnonymous, m.imageSrc , p.memberId, m.nickname, p.modifiedDate, p.category) "
                        + "from Post p join  p.member m "
                +"where p.category = :category "
                                + "group by p.contents having p.contents > :title "
                        + "order by p.createdDate desc "
                        , FindPostDTO.class)
                .setParameter("title", title)
                .setParameter("category", category)
                .setMaxResults(10)
                .getResultList();
    }
    public List<FindPostDTO> findPostsInMyPage(Long memberId) {
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.FindPostDTO(p.id, p.title, p.contents, size(p.postCommentList), p.views, p.isAnonymous, p.contents, p.memberId, m.nickname, p.modifiedDate, p.category ) "
                        + "from Post p, Member m "
                        //+ "join fetch p.member m "
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

    @Transactional
    public void registerPost(RegisterPostDTO r) {
        String anonymous = r.getIsAnonymous() == true ? "Y" : "N";
        entityManager.createNativeQuery("insert into post (title, category, contents, is_anonymous, views, member_id, resume_id) values (?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, r.getTitle() )
                .setParameter(2, String.valueOf(r.getCategory()))
                .setParameter(3, r.getContents())
                .setParameter(4, anonymous)
                .setParameter(5, r.getViews())
                .setParameter(6, r.getMemberId())
                .setParameter(7, r.getResumeId())
                .executeUpdate();
    }

    // 댓글
    @Transactional
    public void registerPostComment(PostCommentDTO pc) {
        String anonymous = pc.getIsAnonymous() == true ? "Y" : "N";
        entityManager.createNativeQuery("insert into post_comment (contents, is_anonymous, post_comment_depth, post_comment_group, member_id, post_id) values (?, ?, ?, ?, ?, ?)")
                .setParameter(1, pc.getContents())
                .setParameter(2, anonymous)
                .setParameter(3, pc.getPostCommentDepth())
                .setParameter(4, pc.getPostCommentGroup())
                .setParameter(5, pc.getMemberId())
                .setParameter(6, pc.getPostId())
                .executeUpdate();
    }

//    todo.대댓글 리스트 반환
//    public List<PostCommentDepthDTO> findCommentDepth(Integer index, Long postId) {
//        // 특정 postid를 가진 commend list 출력
//        // member post id에 해당하는 depth에 해당하는 요소들을 뽑아옴 commendgroup에 해당하는 depthlist 반환
////        public PostCommentDepthDTO(Long memberId, String imageSrc, String nickname, String contents, Integer recommendCnt, Integer banCnt, Boolean isAnonymous, LocalDateTime modifiedDate)
//        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostCommentDepthDTO(pc.memberId, (select mi.imageSrc from MemberInfo mi where mi.member.id = pc.memberId), (select m.nickname from Member m where m.id = pc.memberId), pc.contents, size(pc.postCommentRecommendList), size(pc.postCommentReportList), pc.isAnonymous, pc.createdDate)  "
//                + "from PostComment pc "
//                + "where :postId = pc.postId and :index = pc.postCommentGroup "
//                + "order by pc.createdDate",PostCommentDepthDTO.class )
//                . getResultList();
//    }

    public PostCommentDTO findComment(Integer index, Long postId) {
//        contents, postCommentGroup, postCommentDepth, isAnonymous, Long memberId, Long postId
        return entityManager.createQuery("select new com.f5.resumerry.Post.dto.PostCommentDTO(pc.contents, pc.postCommentGroup, pc.isAnonymous, pc.memberId, pc.postId)  "
                        + "from PostComment pc "
                        + "where :postId = pc.postId and :index = pc.postCommentGroup "
                        + "order by pc.createdDate",PostCommentDTO.class )
                .getSingleResult();
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
