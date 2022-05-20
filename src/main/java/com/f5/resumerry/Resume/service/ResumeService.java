package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberInfoRepository;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Resume.*;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.Resume.repository.*;
import com.f5.resumerry.Reward.repository.TokenHistoryRepository;
import com.f5.resumerry.dto.BooleanResponseDTO;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
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
    private final ResumeCommentRecommendRepository resumeCommentRecommendRepository;
    private final ResumeCommentReportRepository resumeCommentReportRepository;
    private final HashtagRepository hashtagRepository;
    private final ResumeHashtagRepository resumeHashtagRepository;
    private  final MemberInfoRepository memberInfoRepository;

    private final TokenHistoryRepository tokenHistoryRepository;


    public JSONArray viewResumesInMyPage(Long memberId) {
        JSONArray jsonArray = new JSONArray();
        List<Resume> resumeList = resumeRepository.findByMemberId(memberId);
        for(Resume resume: resumeList){
            if(!resume.getIsDelete()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("resumeId", resume.getId());
                jsonObject.put("title", resume.getTitle());
                jsonObject.put("modifiedDate", resume.getModifiedDate().toString());
                jsonObject.put("category", resume.getCategory());
                jsonObject.put("contents", resume.getContents());
                jsonObject.put("fileLink", resume.getFileLink());
                jsonObject.put("viewCnt", resume.getViewCnt());
                jsonObject.put("years", resume.getYears());
                jsonObject.put("memberId", resume.getMemberId());
                JSONArray jsonArray1 = new JSONArray();
                for(ResumeHashtag resumeHashtag: resume.getResumeHashtagList()){
                    jsonArray1.add(resumeHashtag.getHashtag().getHashtagName());
                }
                jsonObject.put("hashtagList", jsonArray1);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    public ViewResumeDTO viewResume(Long memberId, Long resumeId, Long tokenId) {
        // 이력서 세부 조회
        Resume resume = resumeRepository.viewResume(resumeId); // member 와 연관된 모든 값 가져옴
        // 1. 내것이면
        // 1.1 스크랩 불가능 추천 불가능
        // 2. 내것이 아니면
        // 2.1 스크랩을 하였는가
        // 2.2 추천이 되어있는가
        ViewResumeDTO viewResumeDTO = new ViewResumeDTO();
        if(memberId.equals(tokenId)) {
            viewResumeDTO = new ViewResumeDTO(resume, true, false, false);
        } else {
            if (resumeScrapRepository.existsByResume(resume)) {
                // 스크랩이 존재한다면
                if (resumeRecommendRepository.existsByResume(resume)) {
                    viewResumeDTO = new ViewResumeDTO(resume, false, true, true);
                }
                viewResumeDTO = new ViewResumeDTO(resume, false, true, false);
            } else {
                if (resumeRecommendRepository.existsByResume(resume)) {
                    viewResumeDTO = new ViewResumeDTO(resume, false, false, true);
                }
                viewResumeDTO = new ViewResumeDTO(resume, false, false, false);
            }
        }
        // dto에 hashtagname(string 부여)
            List<String> hashtagLists = new ArrayList<String>();
            // resume hash tag 에서 list 반환
            for(ResumeHashtag resumeHashtag : resumeHashtagRepository.findByResumeId(resumeId)) {
                Long hashtagId = resumeHashtag.getHashtagId();
                Hashtag hashtag = hashtagRepository.findByHashtagId(hashtagId);
                hashtagLists.add(hashtag.getHashtagName());
            }
            viewResumeDTO.setHashtag(hashtagLists);

            return viewResumeDTO;



    }

    public void uploadResume(Long id, String fullFileLink, UploadResumeDTO uploadResumeDTO, List<String> hashtagList) {
        RegisterResumeDTO registerResumeDTO = new RegisterResumeDTO();
        registerResumeDTO.setTitle(uploadResumeDTO.getTitle());
        registerResumeDTO.setContents(uploadResumeDTO.getContents());
        registerResumeDTO.setYears(uploadResumeDTO.getYears());
        registerResumeDTO.setFileLink(fullFileLink);
        registerResumeDTO.setCategory(uploadResumeDTO.getCategory());
        registerResumeDTO.setMemberId(id);
        registerResumeDTO.setIsDelete(false);

        Resume resume = registerResumeDTO.toEntity();

        Resume resumeId = resumeRepository.save(resume);
        log.info(String.valueOf(resumeId.getId()));
        Long resumeIdCheck = resumeId.getId();

        for(String hashtag: hashtagList){

            ResumeHashtagDTO resumeHashtagDTO = new ResumeHashtagDTO();
            try{
                Hashtag check = hashtagRepository.findByHashtagName(hashtag);
                Long checkId = check.getId();
                resumeHashtagDTO.setHashtagId(checkId);

            }catch(Exception e){
                HashtagDTO hashtagDTO = new HashtagDTO();
                hashtagDTO.setHashtagName(hashtag);
                Hashtag hashtag1 = hashtagDTO.toEntity();
                Hashtag hashtag2 = hashtagRepository.save(hashtag1);

                resumeHashtagDTO.setHashtagId(hashtag2.getId());

            }
            resumeHashtagDTO.setResumeId(resumeIdCheck);
            ResumeHashtag resumeHashtag = resumeHashtagDTO.toEntity();
            resumeHashtagRepository.save(resumeHashtag);
        }
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
        } else {
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
    public Boolean deleteResumeComment(Long memberId, Long commentId) {
        Optional<ResumeComment> resumeCommentOptional = resumeCommentRepository.findById(commentId);
        ResumeComment resumeComment = resumeCommentOptional.orElse(null);
        if (resumeComment.getMember().getId() == memberId) {
            resumeCommentRepository.deleteResumeComment(commentId);
        } else {
            return false;
        }
        return true;
    }

    @Transactional
    public Boolean recommendResumeComment(Long memberId, Long commentId) {
        if (resumeCommentRecommendRepository.existsByMemberIdAndResumeCommentId(memberId, commentId)){
            deleteResumeCommentRecommend(memberId, commentId);
        } else{
            saveResumeCommentRecommend(memberId, commentId);
        }
        return true;
    }

    @Transactional
    public void deleteResumeCommentRecommend(Long memberId, Long commentId) {
        resumeCommentRecommendRepository.deleteByMemberIdAndResumeCommentId(memberId, commentId);
        return;
    }


    @Transactional
    public ResumeCommentRecommend saveResumeCommentRecommend(Long memberId, Long commentId) {
        ResumeCommentRecommendDTO resumeCommentRecommendDTO = new ResumeCommentRecommendDTO();
        resumeCommentRecommendDTO.setMemberId(memberId);
        resumeCommentRecommendDTO.setResumeCommentId(commentId);
        ResumeCommentRecommend resumeCommentRecommend = resumeCommentRecommendDTO.toEntity();
        return resumeCommentRecommendRepository.save(resumeCommentRecommend);
    }

    @Transactional
    public Boolean reportResumeComment(Long memberId, Long commentId) {
        if (resumeCommentReportRepository.existsByMemberIdAndResumeCommentId(memberId, commentId)){
            deleteResumeCommentReport(memberId, commentId);
        } else{
            saveResumeCommentReport(memberId, commentId);
        }
        return true;
    }

    @Transactional
    public void deleteResumeCommentReport(Long memberId, Long commentId) {
        resumeCommentReportRepository.deleteByMemberIdAndResumeCommentId(memberId, commentId);
        return;
    }


    @Transactional
    public ResumeCommentReport saveResumeCommentReport(Long memberId, Long commentId) {
        ResumeCommentReportDTO resumeCommentReportDTO = new ResumeCommentReportDTO();
        resumeCommentReportDTO.setMemberId(memberId);
        resumeCommentReportDTO.setResumeCommentId(commentId);
        ResumeCommentReport resumeCommentReport = resumeCommentReportDTO.toEntity();
        return resumeCommentReportRepository.save(resumeCommentReport);
    }

    @Transactional
    public ResumeRecommend saveResumeRecommend(Resume resume, Member member) {
        ResumeRecommendDTO resumeRecommendDTO = new ResumeRecommendDTO();
        resumeRecommendDTO.setResume(resume);
        resumeRecommendDTO.setMember(member);
        ResumeRecommend resumeRecommend = resumeRecommendDTO.toEntity();
        return resumeRecommendRepository.save(resumeRecommend);
    }

    public boolean scrapResume(Long memberId, Long resumeId){
        if (resumeScrapRepository.existsByMemberIdAndResumeId(memberId, resumeId)){
            deleteResumeScrap(resumeId, memberId);
        } else{
            saveResumeScrap(resumeId, memberId);
        }
        return true;
    }

    @Transactional
    public void deleteResumeScrap(Long resumeId, Long memberId) {
        ResumeScrap resumeScrap = resumeScrapRepository.findByResumeIdAndMemberId(resumeId, memberId);
        resumeScrapRepository.deleteById(resumeScrap.getId());
        return;
    }


    @Transactional
    public ResumeScrap saveResumeScrap(Long resumeId, Long memberId) {
        ResumeScrapDTO resumeScrapDTO = new ResumeScrapDTO();
        resumeScrapDTO.setResumeId(resumeId);
        resumeScrapDTO.setMemberId(memberId);
        resumeScrapDTO.setCreatedDate(java.time.LocalDateTime.now());
        log.info(String.valueOf(LocalDateTime.now()));
        log.info(String.valueOf(resumeScrapDTO));
        ResumeScrap resumeScrap = resumeScrapDTO.toEntity();
        return resumeScrapRepository.save(resumeScrap);
    }

    public void updateResume(Long memberId, Long resumeId, UploadResumeDTO uploadResumeDTO, String fullFileNamePath, List<String> hashtagList) {
        String title = uploadResumeDTO.getTitle();
        String contents = uploadResumeDTO.getContents();
        CategoryEnum category = uploadResumeDTO.getCategory();
        Integer years = uploadResumeDTO.getYears();
        Resume resume = resumeRepository.updateResume(memberId, resumeId, title, contents, category, years, fullFileNamePath);

    }

    public List<FilterViewResumeDTO> viewResumes(ResumeFilterDTO resumeFilterDTO, Long memberId) {
        // 해시태그 반영 안됨
        // 해시태그 이름으로 파싱 -> resumeid
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
        List<FilterViewResumeDTO> lists = resumeLists
                .stream()
                .map(resume -> new FilterViewResumeDTO(resume))
                .collect(Collectors.toList());

        // dto에 hashtagname(string 부여)
        for(FilterViewResumeDTO list : lists) {
            List<String> hashtagLists = new ArrayList<String>();
            Long resumeId = list.getResumeId();
            // resume hash tag 에서 list 반환
            for(ResumeHashtag resumeHashtag : resumeHashtagRepository.findByResumeId(resumeId)) {
                Long hashtagId = resumeHashtag.getHashtagId();
                Hashtag hashtag = hashtagRepository.findByHashtagId(hashtagId);
                hashtagLists.add(hashtag.getHashtagName());
            }
            list.setHashtag(hashtagLists);
        }

        return lists;
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

    public BooleanResponseDTO unLockResume(Long memberId, Long resumeId) {
        // return BooleanReponse에 담기
        // 1. 이력서를 잠금해제 할만큼 토큰을 가지고있는가 -> 5개라고 임시 지정
        // 1.1 가지고 있음 ( 토큰 갯수 확인 )
        // 1.1.1 유저의 토큰 갯수만큼 차감
        // 1.1.1.1. 해당 유저가 해당 레줌을 lock한것을 put -> 성공시 성공 resposonse 반환
        // 1.2 가지고 있지 않음 -> 부족한 갯수 반환

        Integer numOfTokenToUnLockResume = 5;

        String reasonOftokenUsing = "used";

        BooleanResponseDTO responseDTO = new BooleanResponseDTO(true);

        Integer numOfTokenUserHas = memberInfoRepository.findByMemberId(memberId);

        if(numOfTokenUserHas < numOfTokenToUnLockResume ) {
            responseDTO.setResult(false);
            return responseDTO;
        }

        Member memberData = memberRepository.findByMemberId(memberId);
        Long memberDataInfoId = memberData.getMemberInfoId();

        memberRepository.updateMemberToken(numOfTokenToUnLockResume, memberDataInfoId);

        // token_history table 관리 여부

        // 해당 이력서 열람을 위해 토큰을 사용했다는 이력 남기는거 toekn histroy에 resume_id 추가했음

        tokenHistoryRepository.insertTokenHistory(memberId, resumeId, reasonOftokenUsing, (long) (numOfTokenUserHas-numOfTokenToUnLockResume));

        return responseDTO;

    }
}
