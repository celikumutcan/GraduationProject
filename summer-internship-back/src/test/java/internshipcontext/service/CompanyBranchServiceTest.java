package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Company;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.PasswordResetTokenService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CompanyBranchService.
 * Tests the saveCompanyBranch() method (success case).
 */
@ExtendWith(MockitoExtension.class)
public class CompanyBranchServiceTest {

    @Mock
    private CompanyBranchRepository companyBranchRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @InjectMocks
    private CompanyBranchService companyBranchService;

    /**
     * TC6: Tests if a new company branch is saved and welcome email is sent.
     */
    @Test
    public void testSaveCompanyBranch_SavesAndSendsEmail() {
        User branchUser = new User();
        branchUser.setUserName("branchuser");

        CompanyBranch branch = new CompanyBranch();
        branch.setId(1);
        branch.setBranchName("Tech Branch");
        branch.setBranchEmail("branch@tech.com");
        branch.setBranchUserName(branchUser);
        branch.setCompanyUserName(new Company());

        when(companyBranchRepository.save(any(CompanyBranch.class))).thenReturn(branch);
        when(passwordResetTokenService.createPasswordResetToken("branchuser")).thenReturn("test-token");

        CompanyBranch result = companyBranchService.saveCompanyBranch(branch);

        assertNotNull(result);
        assertEquals("Tech Branch", result.getBranchName());
        verify(emailService, times(1)).sendCompanyBranchWelcomeEmail(eq("branch@tech.com"), eq("branchuser"), contains("test-token"));
        verify(companyBranchRepository, never()).delete(any());
    }
}