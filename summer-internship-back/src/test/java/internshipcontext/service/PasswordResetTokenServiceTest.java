package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.repository.PasswordResetTokenRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.PasswordResetTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetTokenService tokenService;

    /**
     * TC14: Tests the creation of a password reset token and verifies it's saved with correct data.
     */
    @Test
    public void testCreatePasswordResetToken_SavesToken() {
        // Arrange
        String userName = "testuser";

        // Act
        String generatedToken = tokenService.createPasswordResetToken(userName);

        // Assert
        verify(tokenRepository, times(1)).save(argThat(token ->
                token.getUserName().equals(userName) &&
                        token.getToken().equals(generatedToken) &&
                        token.getExpirationTime().isAfter(LocalDateTime.now())
        ));
    }
}