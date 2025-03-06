package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    /**
     * Default constructor for JPA
     */
    public PasswordResetToken() {
    }

    /**
     * Constructs a PasswordResetToken instance.
     *
     * @param userName The username associated with the token
     * @param token The generated reset token
     * @param expirationTime Expiration time of the token
     */
    public PasswordResetToken(String userName, String token, LocalDateTime expirationTime) {
        this.userName = userName;
        this.token = token;
        this.expirationTime = expirationTime;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * Checks if the token is expired.
     *
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}