package com.apdbank.user.fearture.mail.VerificationToken;

import com.apdbank.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    Optional<VerificationToken> findByUser(User user);

    Optional<VerificationToken> findByTokenAndType(String token, VerificationToken.TokenType tokenType);

}
