package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InitialTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.PasswordResetTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for updateInitialFormStatus() method in InitialTraineeInformationFormService.
 */
@ExtendWith(MockitoExtension.class)
public class InitialTraineeInformationFormServiceTest {

    @Mock
    private InitialTraineeInformationFormRepository formRepository;

    @Mock
    private ApprovedTraineeInformationFormRepository approvedFormRepository;

    @Mock
    private CompanyBranchRepository companyBranchRepository;

    @Mock
    private PasswordResetTokenService tokenService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private InitialTraineeInformationFormService formService;

    /**
     * Test updateInitialFormStatus() with status "Company Approval Waiting".
     */
    @Test
    public void testUpdateInitialFormStatus_SendsCompanyEmail() {
        // Arrange
        int formId = 1;
        String username = "student1";
        String status = "Company Approval Waiting";

        User user = new User();
        user.setUserName(username);

        Student student = new Student();
        student.setUserName(username);
        student.setUsers(user);

        InitialTraineeInformationForm form = new InitialTraineeInformationForm();
        form.setId(formId);
        form.setFillUserName(student);

        CompanyBranch branch = new CompanyBranch();
        branch.setBranchEmail("branch@example.com");
        User branchUser = new User();
        branchUser.setUserName("branchUser");
        branch.setBranchUserName(branchUser);

        ApprovedTraineeInformationForm approvedForm = new ApprovedTraineeInformationForm();
        approvedForm.setCompanyBranch(branch);
        approvedForm.setFillUserName(student);

        when(formRepository.findById(formId)).thenReturn(Optional.of(form));
        when(approvedFormRepository.findTopByFillUserName_UserNameOrderByIdDesc(username)).thenReturn(Optional.of(approvedForm));
        when(companyBranchRepository.findByBranchUserName(branchUser)).thenReturn(Optional.of(branch));
        when(tokenService.createPasswordResetToken("branchUser")).thenReturn("mock-token");

        // Act
        boolean result = formService.updateInitialFormStatus(formId, status);

        // Assert
        assertTrue(result);
        verify(formRepository).updateStatus(formId, status);
        verify(tokenService).createPasswordResetToken("branchUser");
        verify(emailService).sendCompanyBranchWelcomeEmail(eq("branch@example.com"), eq("branchUser"), contains("mock-token"));
    }
}