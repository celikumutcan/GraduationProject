package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Finds a password reset token by token value.
     *
     * @param token The reset token
     * @return An optional containing the token if found
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Finds a password reset token by username.
     *
     * @param userName The username
     * @return An optional containing the token if found
     */
    Optional<PasswordResetToken> findByUserName(String userName);
}