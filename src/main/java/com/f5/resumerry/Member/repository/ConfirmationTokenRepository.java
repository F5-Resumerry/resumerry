package com.f5.resumerry.Member.repository;

import com.f5.resumerry.Member.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,String> {
    Optional<ConfirmationToken> findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId, LocalDateTime now, boolean expired);

    Optional<List<ConfirmationToken>> findAllByReceiverEmail(String receiverEmail);
    Boolean existsByReceiverEmail(String receiverEmail);
}