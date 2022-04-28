package com.f5.resumerry.Member.service;



import com.f5.resumerry.Member.entity.ConfirmationToken;
import com.f5.resumerry.Member.exception.BadRequestException;
import com.f5.resumerry.Member.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    /**
     * 이메일 인증 토큰 생성
     * @return
     */

    public Boolean checkExistsReceiverEmail(String receiverEmail) {
        return confirmationTokenRepository.existsByReceiverEmail(receiverEmail);
    }

    public String createEmailConfirmationToken(String receiverEmail){

        Assert.hasText(receiverEmail,"receiverEmail은 필수 입니다.");

        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(receiverEmail);
        confirmationTokenRepository.save(emailConfirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("http://localhost:8080/valid/email/confirm?token="+emailConfirmationToken.getId());
        emailSenderService.sendEmail(mailMessage);

        return emailConfirmationToken.getId();
    }

    /**
     * 유효한 토큰 가져오기
     * @param confirmationTokenId
     * @return
     */
    public ConfirmationToken findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId){
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(confirmationTokenId, LocalDateTime.now(),false);
        return confirmationToken.orElseThrow(()-> new BadRequestException("error"));
    };

    public List<ConfirmationToken> findAllByReceiverEmail(String receiverEmail){
        Optional<List<ConfirmationToken>> confirmationToken = confirmationTokenRepository.findAllByReceiverEmail(receiverEmail);
        return confirmationToken.orElseThrow(()-> new BadRequestException("error"));
    }
}