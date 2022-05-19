package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeComment;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.ResumeScrap;
import com.f5.resumerry.Resume.dto.*;
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
import java.util.stream.Collectors;

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
        // 이력서 세부 조회
        Resume resume = resumeRepository.viewResume(resumeId); // member 와 연관된 모든 값 가져옴
        // 1. 내것이면
        // 1.1 스크랩 불가능 추천 불가능
        // 2. 내것이 아니면
        // 2.1 스크랩을 하였는가
        // 2.2 추천이 되어있는가
        if(memberId.equals(tokenId)) {
            return new ViewResumeDTO(resume, true, false, false);
        } else {
            if (resumeScrapRepository.existsByResume(resume)) {
                // 스크랩이 존재한다면
                if (resumeRecommendRepository.existsByResume(resume)) {
                    return new ViewResumeDTO(resume, false, true, true);
                }
                return new ViewResumeDTO(resume, false, true, false);
            } else {
                if (resumeRecommendRepository.existsByResume(resume)) {
                    return new ViewResumeDTO(resume, false, false, true);
                }
                return new ViewResumeDTO(resume, false, false, false);
            }
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
        // 해시태그 반영 안됨

        String sort = resumeFilterDTO.getSort();
        String title = resumeFilterDTO.getTitle();
        Integer startYear = resumeFilterDTO.getStartYear();
        Integer endYear = resumeFilterDTO.getEndYear();
        CategoryEnum category = resumeFilterDTO.getCategory();

        List<Resume> resumeLists = resumeRepository.findAllWithMember(title, startYear, endYear, category); //기본 생성 날짜로 반환
        if(sort.equals("view")) {
            resumeLists = resumeRepository.findAllWithMemberByView(title, startYear, endYear, category);
        }
        if(sort.equals("recommend")) {
            resumeLists = resumeRepository.findAllWithMemberByRecommend(title, startYear, endYear, category);
        }
        if(sort.equals("years")) {
            resumeLists = resumeRepository.findAllWithMemberByYears(title, startYear, endYear, category);
        }

        // 받은 resumeList dto로 반환
        return resumeLists
                .stream()
                .map(resume -> new FilterViewResumeDTO(resume))
                .collect(Collectors.toList());
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
                    group.put("commentGroup", resumeComment.getResumeCommentGroup());
                    group.put("commentDepth", resumeComment.getResumeCommentDepth());
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
                depthIn.put("commentGroup", resumeComment.getResumeCommentGroup());
                depthIn.put("commentDepth", resumeComment.getResumeCommentDepth());
                depthIn.put("isOwner", resumeComment.getMember().getAccountName() == accountName ? true : false);
                depthIn.put("isDelete", resumeComment.getIsDelete());
                depth.add(depthIn);
            }
            log.info(String.valueOf(depth));
            if(group.size() > 0) {
                group.put("childComments", depth);
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

    //이력서 잠금 해제  -> 잠금

    public void unLockResume(Long id, Long resumeId) {
        // 1. 이력서를 잠금해제 할만큼 토큰을 가지고있는가
        // 1.1 가지고 있음 ( 토큰 갯수 확인 )
        // 1.1.1 유저의 토큰 갯수만큼 차감
        // 1.1.1.1. 해당 유저가 해당 레줌을 lock한것을 put -> 성공시 성공 resposonse 반환
        // 1.2 가지고 있지 않음 -> 부족한 갯수 반환

    }
}
