package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.PasswordResetToken;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.PasswordResetTokenRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for handling password reset token operations.
 */
@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository,
                                     UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates and saves a password reset token for the given user.
     *
     * @param userName The username requesting the reset
     * @return The generated reset token
     */
    public String createPasswordResetToken(String userName) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(168); // 1 week expiration

        PasswordResetToken resetToken = new PasswordResetToken(userName, token, expirationTime);
        tokenRepository.save(resetToken);

        return token;
    }

    /**
     * Validates the password reset token.
     *
     * @param token The reset token to validate
     * @return The associated PasswordResetToken if valid, otherwise empty
     */
    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOptional = tokenRepository.findByToken(token);

        if (resetTokenOptional.isEmpty()) {
            return Optional.empty();
        }

        PasswordResetToken resetToken = resetTokenOptional.get();

        // Check if token has expired
        if (resetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        return Optional.of(resetToken);
    }

    /**
     * Resets the user's password using the provided reset token.
     *
     * @param token       Password reset token
     * @param newPassword New password to be set
     * @return true if password reset is successful, false otherwise
     */
   /* public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetTokenOptional = validatePasswordResetToken(token);

        if (resetTokenOptional.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = resetTokenOptional.get();
        String userName = resetToken.getUserName();

        Object[] user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the used token
        deletePasswordResetToken(token);

        return true;
    }*/

    /**
     * Deletes a password reset token after successful password reset.
     *
     * @param token The reset token to delete
     */
    public void deletePasswordResetToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }


    /**
     * Encodes the plain password using the password encoder.
     *
     * @param plainPassword The password in plain text
     * @return The encoded password
     */
    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

}