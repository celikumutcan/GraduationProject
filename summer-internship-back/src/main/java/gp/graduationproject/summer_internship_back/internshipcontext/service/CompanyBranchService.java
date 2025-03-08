package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing company branches.
 */
@Service
public class CompanyBranchService {

    private final CompanyBranchRepository companyBranchRepository;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    /**
     * Constructor for dependency injection.
     *
     * @param companyBranchRepository Repository for company branches
     * @param emailService Service for sending emails
     * @param passwordResetTokenService Service for managing password reset tokens
     */
    public CompanyBranchService(CompanyBranchRepository companyBranchRepository, EmailService emailService, PasswordResetTokenService passwordResetTokenService) {
        this.companyBranchRepository = companyBranchRepository;
        this.emailService = emailService;
        this.passwordResetTokenService = passwordResetTokenService;
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
     * Retrieves a company branch by its ID.
     *
     * @param id ID of the company branch
     * @return Optional containing the company branch if found
     */
    public Optional<CompanyBranch> getCompanyBranchById(Integer id) {
        return companyBranchRepository.findById(id);
    }

    /**
     * Deletes a company branch by its ID.
     *
     * @param id ID of the company branch to be deleted
     */
    public void deleteCompany(Integer id) {
        companyBranchRepository.deleteById(id);
    }
}