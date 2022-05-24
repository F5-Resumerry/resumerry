package com.f5.resumerry.Resume.service;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.repository.MemberInfoRepository;
import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Resume.dto.GetCommentDTO;
import com.f5.resumerry.Resume.*;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.Resume.repository.*;
import com.f5.resumerry.Reward.ResumeAuthority;
import com.f5.resumerry.Reward.repository.ResumeAuthorityRepository;
import com.f5.resumerry.Reward.repository.TokenHistoryRepository;
import com.f5.resumerry.dto.BooleanResponseDTO;
import com.f5.resumerry.selector.CategoryEnum;
import com.querydsl.core.Tuple;
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
    private final MemberInfoRepository memberInfoRepository;
    private final TokenHistoryRepository tokenHistoryRepository;
    private final ResumeAuthorityRepository resumeAuthorityRepository;
    private final ResumeRecommendCustomRepository resumeRecommendCustomRepository;

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


        ViewResumeDTO viewResumeDTO;
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

        resumeRepository.viewCnt(memberId, resumeId);


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
        registerResumeDTO.setIsDelete(true);
        registerResumeDTO.setViewCnt(0);

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
        memberInfoRepository.updateReward(id, 5, 1);
        String reason = uploadResumeDTO.getTitle() + " 이력서 작성 보상";
        tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
    }

    public void deleteResume (Long memberId, Long postId) {
        //is_delete 컬럼 기본값 n update 형식으로 y로 수정
        resumeRepository.updateIsDelete(memberId, postId);
    }


    public boolean recommendResume(Long memberId, Long resumeId){
        ResumeRecommendDTO resumeRecommendDTO = new ResumeRecommendDTO();
        resumeRecommendDTO.setResumeId(resumeId);
        resumeRecommendDTO.setMemberId(memberId);
        ResumeRecommend resumeRecommend = resumeRecommendDTO.toEntity();
        resumeRecommendRepository.save(resumeRecommend);

        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        Resume resume = resumeOptional.orElse(null);
        Integer recommendCnt = resume.getResumeRecommendList().size();
        if(recommendCnt % 5 == 0){
            Long id = resume.getMemberId();
            memberInfoRepository.updateReward(id, 5, 1);
            String reason = resume.getTitle() + " 이력서 추천 " + recommendCnt + "개 달성 보상";
            tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
        }
        return true;
    }


    // 댓글 controller 시작
    @Transactional
    public void registerResumeComment(Long memberId, Long resumeId, GetCommentDTO req) {
        ResumeCommentDTO resumeCommentDTO = new ResumeCommentDTO();
        resumeCommentDTO.setContents(req.getContents());
        resumeCommentDTO.setIsAnonymous(!req.getIsAnonymous());
        resumeCommentDTO.setResumeCommentGroup(req.getCommentGroup());
        resumeCommentDTO.setResumeCommentDepth(req.getCommentDepth());
        resumeCommentDTO.setMemberId(memberId);
        resumeCommentDTO.setResumeId(resumeId);
        resumeCommentDTO.setIsDelete("N");
        ResumeComment resumeComment = resumeCommentDTO.toEntity();
        resumeCommentRepository.save(resumeComment);
        memberInfoRepository.updateReward(memberId, 5, 0);
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
        ResumeCommentRecommendDTO resumeCommentRecommendDTO = new ResumeCommentRecommendDTO();
        resumeCommentRecommendDTO.setMemberId(memberId);
        resumeCommentRecommendDTO.setResumeCommentId(commentId);
        ResumeCommentRecommend resumeCommentRecommend = resumeCommentRecommendDTO.toEntity();
        resumeCommentRecommendRepository.save(resumeCommentRecommend);
        Optional<ResumeComment> resumeCommentOptional = resumeCommentRepository.findById(commentId);
        ResumeComment resumeComment = resumeCommentOptional.orElse(null);
        Integer commentRecommendCnt = resumeComment.getResumeCommentRecommendList().size();
        if(commentRecommendCnt % 5 == 0){
            Long id = resumeComment.getMemberId();
            memberInfoRepository.updateReward(id, 5, 1);
            String reason = resumeComment.getContents() + " 답변 추천 " + commentRecommendCnt + "개 달성 보상";
            tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
        }
        return true;
    }

    @Transactional
    public Boolean reportResumeComment(Long memberId, Long commentId) {
        ResumeCommentReportDTO resumeCommentReportDTO = new ResumeCommentReportDTO();
        resumeCommentReportDTO.setMemberId(memberId);
        resumeCommentReportDTO.setResumeCommentId(commentId);
        ResumeCommentReport resumeCommentReport = resumeCommentReportDTO.toEntity();
        resumeCommentReportRepository.save(resumeCommentReport);
        return true;
    }

    @Transactional
    public boolean scrapResume(Long memberId, Long resumeId){
        ResumeScrapDTO resumeScrapDTO = new ResumeScrapDTO();
        resumeScrapDTO.setResumeId(resumeId);
        resumeScrapDTO.setMemberId(memberId);
        resumeScrapDTO.setCreatedDate(java.time.LocalDateTime.now());
        ResumeScrap resumeScrap = resumeScrapDTO.toEntity();
        resumeScrapRepository.save(resumeScrap);

        Optional<Resume> resumeOptional = resumeRepository.findById(resumeId);
        Resume resume = resumeOptional.orElse(null);
        Integer scrapCnt = resume.getResumeScrapList().size();
        if(scrapCnt % 5 == 0){
            Long id = resume.getMemberId();
            memberInfoRepository.updateReward(id, 5, 1);
            String reason = resume.getTitle() + " 이력서 스크랩 " + scrapCnt + "개 달성 보상";
            tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
        }

        return true;
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

    public JSONArray viewComments(Long resumeId, Long memberId) {

        ArrayList<Long>[] arrayList = new ArrayList[100];
        for(int i = 0; i <  100; i++){
            arrayList[i] = new ArrayList<Long>();
        }
        List<ResumeComment> resumeComments = resumeCommentRepository.findByResumeId(resumeId);
        for(ResumeComment resumeComment: resumeComments){
            arrayList[resumeComment.getResumeCommentGroup()].add(resumeComment.getId());
        }
        JSONArray jsonArray = new JSONArray();
        for(ArrayList arrayList1: arrayList){
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
                    group.put("isOwner", resumeComment.getMember().getId() == memberId ? true : false);
                    group.put("isDelete", resumeComment.getIsDelete());
                    group.put("isRecommend", resumeCommentRecommendRepository.existsByMemberIdAndResumeCommentId(memberId, resumeComment.getId()));
                    group.put("isBanned", resumeCommentReportRepository.existsByMemberIdAndResumeCommentId(memberId, resumeComment.getId()));
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
                depthIn.put("isOwner", resumeComment.getMember().getId() == memberId ? true : false);
                depthIn.put("isDelete", resumeComment.getIsDelete());
                depthIn.put("isRecommend", resumeCommentRecommendRepository.existsByMemberIdAndResumeCommentId(memberId, resumeComment.getId()));
                depthIn.put("isBanned", resumeCommentReportRepository.existsByMemberIdAndResumeCommentId(memberId, resumeComment.getId()));
                depth.add(depthIn);
            }
            log.info(String.valueOf(depth));
            if(group.size() > 0) {
                group.put("childComments", depth);
                jsonArray.add(group);
            }
        }
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
        Resume resumeData = resumeRepository.getById(resumeId);
        // 해당 이력서 열람을 위해 토큰을 사용했다는 이력 남기는거 token histroy에 resume_id 추가했음
        String reason =resumeData.getMember().getAccountName()+ "  " +resumeData.getTitle()+" 이력서 해제";
        tokenHistoryRepository.insertTokenHistory(memberId, reason, (long) (numOfTokenUserHas- numOfTokenToUnLockResume));
        resumeAuthorityRepository.insertResumeAuthority(memberId, resumeId); // lock 해제 이력 남김

        return responseDTO;

    }

    public List<ResumeRecommendDTO> getAllResumeRecommend(Long userId, Long resumeId) {

        return resumeRecommendCustomRepository.findResumeRecommendByUserId(userId, resumeId);
    }
}
