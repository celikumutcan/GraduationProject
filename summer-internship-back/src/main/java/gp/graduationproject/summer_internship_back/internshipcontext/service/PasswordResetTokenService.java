package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.PasswordResetToken;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Creates and saves a password reset token for the given user.
     *
     * @param userName The username requesting the reset
     * @return The generated reset token
     */
    public String createPasswordResetToken(String userName) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(168); // Token expires in 1 week (168 hour)

        PasswordResetToken resetToken = new PasswordResetToken(userName, token, expirationTime);
        tokenRepository.save(resetToken);

        return token;
    }

    /**
     * Validates a password reset token.
     *
     * @param token The reset token to validate
     * @return The associated PasswordResetToken if valid, otherwise empty
     */
    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOptional = tokenRepository.findByToken(token);

        if (resetTokenOptional.isEmpty()) {
            return Optional.empty(); // Token yok
        }

        PasswordResetToken resetToken = resetTokenOptional.get();
        if (resetToken.isExpired()) {
            throw new RuntimeException("Your reset token has expired. Please request a new one.");
        }

        return Optional.of(resetToken);
    }

    /**
     * Deletes a password reset token after successful password reset.
     *
     * @param token The reset token to delete
     */
    public void deletePasswordResetToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }
}