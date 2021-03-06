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
import com.f5.resumerry.selector.SortingEnum;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Comparator;
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

    private final ResumeRepositorySupport resumeRepositorySupport;

    public List<FilterViewResumeDTO> viewResumesInMyPage(Long memberId) {
        List<FilterViewResumeDTO> lists = resumeRepository.viewResumesInMyPage(memberId);
        for(FilterViewResumeDTO list : lists) {
            List<String> hashtagLists = new ArrayList<String>();
            Long resumeId = list.getResumeId();
            // resume hash tag ?????? list ??????
            for(ResumeHashtag resumeHashtag : resumeHashtagRepository.findByResumeId(resumeId)) {
                Long hashtagId = resumeHashtag.getHashtagId();
                hashtagLists.add(resumeHashtag.getHashtag().getHashtagName());
            }
            list.setHashtag(hashtagLists);
        }
        return lists;
    }

    public ViewResumeDTO viewResume(Long memberId, Long resumeId, Long tokenId) {
        // ????????? ?????? ??????
        Resume resume = resumeRepository.viewResume(resumeId); // member ??? ????????? ?????? ??? ?????????
        // 1. ????????????
        // 1.1 ????????? ????????? ?????? ?????????
        // 2. ????????? ?????????
        // 2.1 ???????????? ????????????
        // 2.2 ????????? ???????????????
        ViewResumeDTO viewResumeDTO;
        Boolean isBuyer;
        Boolean isOwner;
        Boolean isScrap;
        Boolean isRecommend;
        if(resumeAuthorityRepository.existsByMemberIdAndResumeId(tokenId, resumeId)) {
            isBuyer = true;
        } else {
            isBuyer = false;
        }

        if(memberId.equals(tokenId)) {
            isOwner = true;

        } else {
            isOwner = false;
        }

        if (resumeScrapRepository.existsByResumeIdAndMemberId(resumeId, tokenId)) {
            isScrap = true;
        } else {
            isScrap = false;
        }
        if (resumeRecommendRepository.existsByResumeIdAndMemberId(resumeId, tokenId)) {
            isRecommend = true;
        } else {
            isRecommend = false;
        }
        viewResumeDTO = new ViewResumeDTO(resume, isOwner, isScrap, isRecommend, isBuyer);
        // dto??? hashtagname(string ??????)
            List<String> hashtagLists = new ArrayList<String>();
            // resume hash tag ?????? list ??????
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
        registerResumeDTO.setIsLock(true);


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
        String reason = uploadResumeDTO.getTitle() + " ????????? ?????? ??????";
        tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
    }

    public void deleteResume (Long memberId, Long postId) {
        //is_delete ?????? ????????? n update ???????????? y??? ??????
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
            String reason = resume.getTitle() + " ????????? ?????? " + recommendCnt + "??? ?????? ??????";
            tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
        }
        if(resume.getIsLock() == true){
            log.info(String.valueOf(resume.getIsLock()));
            return true;
        }
        if(recommendCnt > 10){
            resumeRepository.lockResume(resumeId);
        }
        return true;
    }


    // ?????? controller ??????
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
        resumeCommentDTO.setYPath(req.getYPath());
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
            String reason = resumeComment.getContents() + " ?????? ?????? " + commentRecommendCnt + "??? ?????? ??????";
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
            String reason = resume.getTitle() + " ????????? ????????? " + scrapCnt + "??? ?????? ??????";
            tokenHistoryRepository.insertTokenHistory(id, reason, 1L);
        }

        return true;
    }


    public void updateResume(Long memberId, Long resumeId, UploadResumeDTO uploadResumeDTO, String fullFileNamePath, List<String> hashtagList) {
        String title = uploadResumeDTO.getTitle();
        String contents = uploadResumeDTO.getContents();
        CategoryEnum category = uploadResumeDTO.getCategory();
        Integer years = uploadResumeDTO.getYears();
        resumeRepository.updateResume(memberId, resumeId, title, contents, category, years, fullFileNamePath);

    }

    public ResumesFullResponse viewResumes(ResumeFilterDTO resumeFilterDTO) {
        // ???????????? ?????? ??????
        // ???????????? ???????????? ?????? -> resumeid
        ResumesFullResponse resumesFullResponse = new ResumesFullResponse();
        String sort = resumeFilterDTO.getSort();
        String title = resumeFilterDTO.getTitle();
        Integer startYear = resumeFilterDTO.getStartYear();
        Integer endYear = resumeFilterDTO.getEndYear();
        CategoryEnum category = resumeFilterDTO.getCategory();
        Integer pageNo = resumeFilterDTO.getPageNo();

        Pageable paging = PageRequest.of(pageNo, 20, Sort.by("createdDate").descending()) ;

        if(SortingEnum.VIEW.toString().equalsIgnoreCase(sort)) {
            paging = PageRequest.of(pageNo, 20, Sort.by("viewCnt").descending()) ;
        }

        if(SortingEnum.YEARS.toString().equalsIgnoreCase(sort)) {
            paging = PageRequest.of(pageNo, 20, Sort.by("years").descending()) ;
        }

        PageImpl<FilterViewResumeDTO> lists = new PageImpl<>(new ArrayList<>());

        if (category.equals(CategoryEnum.ALL)) {
             lists = resumeRepositorySupport.findAllResumes(paging, title, startYear, endYear);
        }
        else{
             lists = resumeRepositorySupport.findCategoryResumes(paging, title, category, startYear, endYear);
        }
                // dto??? hashtagname(string ??????) -> ???????????? ??????,,
        for(FilterViewResumeDTO list : lists) {
            List<String> hashtagLists = new ArrayList<String>();
            Long resumeId = list.getResumeId();
            // resume hash tag ?????? list ??????
            for(ResumeHashtag resumeHashtag : resumeHashtagRepository.findByResumeId(resumeId)) {
                Long hashtagId = resumeHashtag.getHashtagId();
                hashtagLists.add(resumeHashtag.getHashtag().getHashtagName());
            }
            list.setHashtag(hashtagLists);
        }


        if(SortingEnum.RECOMMEND.toString().equalsIgnoreCase(sort)) {
              return new ResumesFullResponse(lists.stream().sorted(Comparator.comparing(FilterViewResumeDTO::getRecommendCnt).reversed()).collect(Collectors.toList()), lists.getTotalPages());
        }
        resumesFullResponse = new ResumesFullResponse(lists.getContent(), lists.getTotalPages());
        return resumesFullResponse;

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
                    group.put("yPath", resumeComment.getYPath());
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
                depthIn.put("yPath", resumeComment.getYPath());
                depth.add(depthIn);
            }
            if(group.size() > 0) {
                group.put("childComments", depth);
                jsonArray.add(group);
            }
        }
        return jsonArray;
    }

    //????????? ?????? ??????  -> ??????

    public BooleanResponseDTO unLockResume(Long memberId, Long resumeId) {
        // return BooleanReponse??? ??????
        // 1. ???????????? ???????????? ????????? ????????? ?????????????????? -> 5????????? ?????? ??????
        // 1.1 ????????? ?????? ( ?????? ?????? ?????? )
        // 1.1.1 ????????? ?????? ???????????? ??????
        // 1.1.1.1. ?????? ????????? ?????? ????????? lock????????? put -> ????????? ?????? resposonse ??????
        // 1.2 ????????? ?????? ?????? -> ????????? ?????? ??????

        Integer numOfTokenToUnLockResume = 5;

        BooleanResponseDTO responseDTO = new BooleanResponseDTO(true);

        Integer numOfTokenUserHas = memberInfoRepository.findByMemberId(memberId);

        if(numOfTokenUserHas < numOfTokenToUnLockResume ) {
            responseDTO.setResult(false);
            return responseDTO;
        }

        Member memberData = memberRepository.findByMemberId(memberId);
        Long memberDataInfoId = memberData.getMemberInfoId();

        memberRepository.updateMemberToken(-1 * numOfTokenToUnLockResume, memberDataInfoId);

        // token_history table ?????? ??????
        Resume resumeData = resumeRepository.getById(resumeId);
        // ?????? ????????? ????????? ?????? ????????? ??????????????? ?????? ???????????? token histroy??? resume_id ????????????
        String reason = resumeData.getMember().getAccountName() + "  " + resumeData.getTitle()+" ????????? ??????";
        tokenHistoryRepository.insertTokenHistory(memberId, reason, (long) (numOfTokenUserHas- numOfTokenToUnLockResume));
        resumeAuthorityRepository.insertResumeAuthority(memberId, resumeId); // lock ?????? ?????? ??????

        return responseDTO;

    }

    public List<ResumeSimilarRecommendDto> getAllResumeRecommend(Long userId, Long resumeId) {

        return resumeRecommendCustomRepository.findResumeRecommendByResumeId(userId, resumeId);
    }
}
