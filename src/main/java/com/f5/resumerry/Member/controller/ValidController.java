package com.f5.resumerry.Member.controller;

import com.f5.resumerry.Member.domain.dto.AccountNameDTO;
import com.f5.resumerry.Member.domain.dto.ConfirmationTokenDTO;
import com.f5.resumerry.Member.domain.dto.NicknameDTO;
import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.exception.DuplicateException;
import com.f5.resumerry.exception.ErrorCode;
import com.f5.resumerry.Member.service.ConfirmationTokenService;
import com.f5.resumerry.Member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/valid")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ValidController {

    private final MemberServiceImpl memberServiceImpl;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/account/exists")
    public ResponseEntity<Map<String, Boolean>> checkAccountNameDuplicate(@RequestBody AccountNameDTO memberDTO) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("result", memberServiceImpl.checkExistsAccountName(memberDTO.getAccountName()));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/nickname/exists")
    public ResponseEntity<Map<String, Boolean>> checkNickNameDuplicate(@RequestBody NicknameDTO memberDTO) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("result", memberServiceImpl.checkExistsNickname(memberDTO.getNickname()));
        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/email/send")
    public ResponseEntity<Map<String, Boolean>> emailSend(@Valid @RequestBody ConfirmationTokenDTO confirmationTokenDTO) {
        if(ResponseEntity.ok(confirmationTokenService.checkExistsReceiverEmail(confirmationTokenDTO.getReceiverEmail())).getBody().booleanValue() == false){
            confirmationTokenService.createEmailConfirmationToken(confirmationTokenDTO.getReceiverEmail());
        }
        else{
            if(memberServiceImpl.checkEmail(confirmationTokenDTO.getReceiverEmail()) == false){
                confirmationTokenService.createEmailConfirmationToken(confirmationTokenDTO.getReceiverEmail());
            }
            else {
                throw new DuplicateException("receiverEmail", "receiverEmail duplicated", ErrorCode.DUPLICATION);
            }
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", true);
        return ResponseEntity.ok().body(result);
        //????????? ????????????, ?????? ?????? ??? ?????? ?????? ??? ????????? ??????
    }

    @GetMapping("/email/confirm")
    public String confirmEmail(@RequestParam String token){
        memberServiceImpl.confirmEmail(token);
        return "????????? ????????? ?????????????????????";
    }

    @PostMapping("/email/check")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@Valid @RequestBody ConfirmationTokenDTO confirmationTokenDTO){
        Map<String, Boolean> result = new HashMap<>();
        result.put("result", memberServiceImpl.checkEmail(confirmationTokenDTO.getReceiverEmail()));
        return ResponseEntity.ok().body(result);
    }

}