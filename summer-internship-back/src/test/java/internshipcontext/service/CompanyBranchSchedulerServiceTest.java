package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InactiveCompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.EvaluateFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchSchedulerService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit test for CompanyBranchSchedulerService.
 * Tests the sendVerificationEmails() method.
 */
@ExtendWith(MockitoExtension.class)
public class CompanyBranchSchedulerServiceTest {

    @Mock
    private CompanyBranchRepository companyBranchRepository;

    @Mock
    private InactiveCompanyBranchRepository inactiveCompanyBranchRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;

    @Mock
    private EvaluateFormRepository evaluateFormRepository;

    @InjectMocks
    private CompanyBranchSchedulerService schedulerService;

    /**
     * TC5: Tests if verification emails are sent to all company branches.
     */
    @Test
    public void testSendVerificationEmails_SendsEmailsToAllBranches() {
        // Arrange
        CompanyBranch branch1 = new CompanyBranch();
        branch1.setId(1);
        branch1.setBranchName("ABC Company");
        branch1.setBranchEmail("abc@company.com");

        CompanyBranch branch2 = new CompanyBranch();
        branch2.setId(2);
        branch2.setBranchName("XYZ Corp");
        branch2.setBranchEmail("xyz@corp.com");

        List<CompanyBranch> branches = Arrays.asList(branch1, branch2);

        when(companyBranchRepository.findAll()).thenReturn(branches);

        // Act
        schedulerService.sendVerificationEmails();

        // Assert
        verify(emailService, times(2)).sendEmail(anyString(), eq("Annual Company Branch Verification"), anyString());
    }
}