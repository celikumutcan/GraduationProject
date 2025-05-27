package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service for managing company branches.
 */
@Service
public class CompanyBranchService {

    private final CompanyBranchRepository companyBranchRepository;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param companyBranchRepository Repository for company branches
     * @param emailService Service for sending emails
     * @param passwordResetTokenService Service for managing password reset tokens
     */
    public CompanyBranchService(CompanyBranchRepository companyBranchRepository, EmailService emailService, PasswordResetTokenService passwordResetTokenService, UserRepository userRepository) {
        this.companyBranchRepository = companyBranchRepository;
        this.emailService = emailService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.userRepository = userRepository;
    }

    /**
     * Saves a new company branch and sends a welcome email with a password reset link.
     * If the email fails to send, the branch is removed from the database.
     *
     * @param companyBranch The company branch to be saved
     * @return The saved company branch entity
     */
    public CompanyBranch saveCompanyBranch(CompanyBranch companyBranch) {
        CompanyBranch savedBranch = companyBranchRepository.save(companyBranch);

        String userName = savedBranch.getBranchUserName().getUserName();
        String resetToken = passwordResetTokenService.createPasswordResetToken(userName);
        String resetLink = "https://your-app.com/reset-password?token=" + resetToken;

        try {
            emailService.sendCompanyBranchWelcomeEmail(
                    savedBranch.getBranchEmail(),
                    userName,
                    resetLink
            );
        } catch (Exception e) {
            companyBranchRepository.delete(savedBranch);
            throw new RuntimeException("Failed to send email. Registration aborted.", e);
        }

        return savedBranch;
    }

    /**
     * Retrieves all branches of a given company.
     *
     * @param userName Username of the company
     * @return List of company branches
     */
    public List<CompanyBranch> getAllCompanyBranchesofCompany(String userName) {
        return companyBranchRepository.findAllByCompanyUserName_UserName(userName);
    }


    /**
     * Generates a secure 12-character random password using letters, numbers, and symbols.
     *
     * @return A newly generated plain text password
     */
    public String generateRandomPassword() {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.-+!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            password.append(charset.charAt(random.nextInt(charset.length())));
        }
        return password.toString();
    }


    /**
     * Sends a reset password email to the company branch.
     *
     * @param recipientEmail Email of the company branch
     * @param userName Branch username
     * @param plainPassword Newly generated password
     */
    public void sendResetPasswordToCompanyBranch(String recipientEmail, String userName, String plainPassword) {
        emailService.sendCompanyBranchResetPasswordEmail(recipientEmail, userName, plainPassword);
    }

    public Optional<Integer> getBranchIdByUsername(String username) {
        return companyBranchRepository.findBranchIdByUsername(username);
    }
}