package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Post.dto.PostCommentDTO;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
import com.f5.resumerry.Resume.ResumeComment;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.ResumeScrap;
import com.f5.resumerry.Resume.repository.ResumeCommentRepository;
import com.f5.resumerry.Resume.repository.ResumeRecommendRepository;
import com.f5.resumerry.Resume.repository.ResumeRepository;
import com.f5.resumerry.Resume.repository.ResumeScrapRepository;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    private final MemberRepository memberRepository;
    private final ResumeRecommendRepository resumeRecommendRepository;
    private final ResumeScrapRepository resumeScrapRepository;
    private final ResumeCommentRepository resumeCommentRepository;

    public List<ResumeDTO> viewResumesInMyPage(Long memberId) {
        return resumeRepository.viewResumesInMyPage(memberId);
    }

    public ViewResumeDTO viewResume(Long memberId, Long resumeId, Long tokenId) {
        resumeRepository.viewCnt(memberId, resumeId); // view cnt +
        // 소유자가 아닌경우 isOwner = false
        if (!memberId.equals(tokenId)) {
                // 스크랩 기록이 없는 경우  isScrap = false , isOwner = false
                if(!resumeRepository.existScrapByMemberIdAndResumeId(tokenId, resumeId))
                    return resumeRepository.noOwnerResumeAndNoScrap(tokenId, resumeId);
                // 스크랩이 있는경우 isScrap = true , isOwner = false
                return resumeRepository.noOwnerResumeCanScrap(tokenId, resumeId);

        } else {
            // 내꺼인 경우 isOwner = true isScrap = false
            return resumeRepository.viewResumeMine(tokenId,resumeId);
        }
    }

    public void uploadResume(Long id, String fullFileLink, UploadResumeDTO uploadResumeDTO) {
        String title = uploadResumeDTO.getTitle();
        String contents = uploadResumeDTO.getContents();
        CategoryEnum category = uploadResumeDTO.getCategory();
        Integer years = uploadResumeDTO.getYears();
        resumeRepository.uploadResume(id, fullFileLink, title, contents, category, years);
    }

    public void deleteResume (Long memberId, Long postId) {
        //is_delete 컬럼 기본값 n update 형식으로 y로 수정
        resumeRepository.updateIsDelete(memberId, postId);
    }

    public boolean recommendResume(String accountName, Long resumeId){
        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        Member member = memberRepository.findByAccountName(accountName);
        Resume resume = resumeOptional.orElse(null);
        if (resumeRecommendRepository.existsByMemberAndResume(member, resume)){
            deleteResumeRecommend(resume, member);
        } else{
            saveResumeRecommend(resume, member);
        }
        return true;
    }

    @Transactional
    public void deleteResumeRecommend(Resume resume, Member member) {
        ResumeRecommend resumeRecommend = resumeRecommendRepository.findByResumeAndMember(resume, member);
        resumeRecommendRepository.deleteById(resumeRecommend.getId());
        return;
    }


    // 댓글 controller 시작
    @Transactional
    public void registerResumeComment(Long memberId, Long resumeId, GetCommentDTO req) {
        ResumeCommentDTO resumeCommentDTO = new ResumeCommentDTO();
        Member member = memberRepository.getById(memberId);
        Resume resume = resumeRepository.getById(resumeId);

        resumeCommentDTO.setContents(req.getContents());
        resumeCommentDTO.setIsAnonymous(req.getIsAnonymous());
        resumeCommentDTO.setResumeCommentGroup(req.getPostCommentGroup());
        resumeCommentDTO.setResumeCommentDepth(req.getPostCommentDepth());
        resumeCommentDTO.setMember(member);
        resumeCommentDTO.setResume(resume);
        resumeCommentDTO.setIsDelete("N");
        ResumeComment resumeComment = resumeCommentDTO.toEntity();
        resumeCommentRepository.save(resumeComment);
    }

    @Transactional
    public ResumeRecommend saveResumeRecommend(Resume resume, Member member) {
        ResumeRecommendDTO resumeRecommendDTO = new ResumeRecommendDTO();
        resumeRecommendDTO.setResume(resume);
        resumeRecommendDTO.setMember(member);
        ResumeRecommend resumeRecommend = resumeRecommendDTO.toEntity();
        return resumeRecommendRepository.save(resumeRecommend);
    }

    public boolean scrapResume(String accountName, Long resumeId){
        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        Member member = memberRepository.findByAccountName(accountName);
        Resume resume = resumeOptional.orElse(null);
        if (resumeScrapRepository.existsByMemberAndResume(member, resume)){
            deleteResumeScrap(resume, member);
        } else{
            saveResumeScrap(resume, member);
        }
        return true;
    }

    @Transactional
    public void deleteResumeScrap(Resume resume, Member member) {
        ResumeScrap resumeScrap = resumeScrapRepository.findByResumeAndMember(resume, member);
        resumeScrapRepository.deleteById(resumeScrap.getId());
        return;
    }


    @Transactional
    public ResumeScrap saveResumeScrap(Resume resume, Member member) {
        ResumeScrapDTO resumeScrapDTO = new ResumeScrapDTO();
        resumeScrapDTO.setResume(resume);
        resumeScrapDTO.setMember(member);
        ResumeScrap resumeScrap = resumeScrapDTO.toEntity();
        return resumeScrapRepository.save(resumeScrap);
    }

    public void updateResume(Long memberId, Long resumeId, UploadResumeDTO uploadResumeDTO, String fullFileNamePath) {
        String title = uploadResumeDTO.getTitle();
        String contents = uploadResumeDTO.getContents();
        CategoryEnum category = uploadResumeDTO.getCategory();
        Integer years = uploadResumeDTO.getYears();
        resumeRepository.updateResume(memberId, resumeId, title, contents, category, years, fullFileNamePath);
    }

    public List<FilterViewResumeDTO> viewResumes(ResumeFilterDTO resumeFilterDTO, Long memberId) {
        return resumeRepository.examViewResumes(memberId);
    }

    public JSONArray viewComments(Long resumeId, String accountName) {

        ArrayList<Long>[] arrayList = new ArrayList[100];
        for(int i = 0; i <  100; i++){
            arrayList[i] = new ArrayList<Long>();
        }
        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        log.info("hi\n");
        Resume resume = resumeOptional.orElse(null);
        log.info("hi\n");
        List<ResumeComment> resumeComments = resumeCommentRepository.findByResume(resume);
        for(ResumeComment resumeComment: resumeComments){
            arrayList[resumeComment.getResumeCommentGroup()].add(resumeComment.getId());
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(ArrayList arrayList1: arrayList){
//            if(!arrayList1.isEmpty()){
//                break;
//            }
            int count = 0;
            JSONObject group = new JSONObject();
            JSONArray depth = new JSONArray();

            for(Object id: arrayList1){
                Optional<ResumeComment> resumeCommentOptional = resumeCommentRepository.findById((Long) id);
                ResumeComment resumeComment = resumeCommentOptional.orElse(null);
                JSONObject depthIn = new JSONObject();
                if(count == 0){
                    group.put("commentId", id);
                    group.put("memberId", resumeComment.getMember().getId());
                    group.put("imageSrc", resumeComment.getMember().getImageSrc());
                    group.put("nickname", resumeComment.getMember().getNickname());
                    group.put("contents", resumeComment.getContents());
                    group.put("recommendCnt", resumeComment.getResumeCommentRecommendList().size());
                    group.put("banCnt", resumeComment.getResumeCommentReportList().size());
                    group.put("isAnonymous", resumeComment.getIsAnonymous());
                    group.put("modifiedDate", resumeComment.getModifiedDate().toString());
                    group.put("resumeCommentGroup", resumeComment.getResumeCommentGroup());
                    group.put("resumeCommentDepth", resumeComment.getResumeCommentDepth());
                    group.put("isOwner", resumeComment.getMember().getAccountName() == accountName ? true : false);
                    group.put("isDelete", resumeComment.getIsDelete());
                    count += 1;
                    continue;
                }
                depthIn.put("commentId", id);
                depthIn.put("memberId", resumeComment.getMember().getId());
                depthIn.put("imageSrc", resumeComment.getMember().getImageSrc());
                depthIn.put("nickname", resumeComment.getMember().getNickname());
                depthIn.put("contents", resumeComment.getContents());
                depthIn.put("recommendCnt", resumeComment.getResumeCommentRecommendList().size());
                depthIn.put("banCnt", resumeComment.getResumeCommentReportList().size());
                depthIn.put("isAnonymous", resumeComment.getIsAnonymous());
                depthIn.put("modifiedDate", resumeComment.getModifiedDate().toString());
                depthIn.put("resumeCommentGroup", resumeComment.getResumeCommentGroup());
                depthIn.put("resumeCommentDepth", resumeComment.getResumeCommentDepth());
                depthIn.put("isOwner", resumeComment.getMember().getAccountName() == accountName ? true : false);
                depthIn.put("isDelete", resumeComment.getIsDelete());
                depth.add(depthIn);
            }
            log.info(String.valueOf(depth));
            if(group.size() > 0) {
                group.put("resumeChildComments", depth);
                jsonArray.add(group);
            }
            log.info("hi\n");
        }

        log.info(String.valueOf(arrayList));
        log.info("hello\n");
        log.info(String.valueOf(resumeComments));
        jsonObject.put("hello", "hello");
        return jsonArray;
    }
}
