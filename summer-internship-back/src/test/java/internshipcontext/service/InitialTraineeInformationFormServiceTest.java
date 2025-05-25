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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for the updateInitialFormStatus() method in InitialTraineeInformationFormService.
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InitialTraineeInformationFormService formService;

    /**
     * Tests that when the form status is updated to "Company Approval Waiting",
     * the company receives an email with credentials.
     */
    @Test
    public void testUpdateInitialFormStatus_SendsCompanyEmail() {
        int formId = 1;
        String username = "student1";
        String status = "Company Approval Waiting";

        User user = new User();
        user.setUserName(username);
        user.setEmail("student@example.com");

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
        when(tokenService.encodePassword(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(branchUser);
        when(userRepository.findAllStudentAffairs()).thenReturn(List.of());

        boolean result = formService.updateInitialFormStatus(formId, status);

        assertTrue(result);
        verify(formRepository).updateStatus(formId, status);
        verify(tokenService).encodePassword(anyString());
        verify(emailService).sendCompanyBranchWelcomeEmail(eq("branch@example.com"), eq("branchUser"), anyString());
    }
}