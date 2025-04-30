package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ApprovedTraineeInformationFormService.
 * Covers simple retrieval, update, and approval logic with mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
public class ApprovedTraineeInformationFormServiceTest {

    @Mock
    private ApprovedTraineeInformationFormRepository formRepository;

    @Mock
    private InsuranceApprovalLogRepository insuranceApprovalLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ApprovedTraineeInformationFormService formService;

    /**
     * TC2: Tests getApprovedTraineeInformationForms() method.
     */
    @Test
    public void testGetApprovedTraineeInformationForms_ReturnsList() {
        // Arrange
        ApprovedTraineeInformationForm form1 = new ApprovedTraineeInformationForm();
        ApprovedTraineeInformationForm form2 = new ApprovedTraineeInformationForm();
        List<ApprovedTraineeInformationForm> mockForms = Arrays.asList(form1, form2);

        when(formRepository.findAll()).thenReturn(mockForms);

        // Act
        List<ApprovedTraineeInformationForm> result = formService.getApprovedTraineeInformationForms();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * TC3: Tests updateFormStatus() method when form exists.
     */
    @Test
    public void testUpdateFormStatus_ReturnsTrue_WhenFormExists() {
        // Arrange
        ApprovedTraineeInformationForm form = new ApprovedTraineeInformationForm();
        form.setStatus("Pending");

        when(formRepository.findById(1)).thenReturn(Optional.of(form));

        // Act
        boolean result = formService.updateFormStatus(1, "Approved");

        // Assert
        assertTrue(result);
        assertEquals("Approved", form.getStatus());
        verify(formRepository).save(form);
    }

    /**
     * TC4: Tests approveInsurance() method updates status and logs.
     */
    @Test
    public void testApproveInsurance_UpdatesInternshipAndLogsApproval() {
        // Arrange
        ApprovedTraineeInformationForm internship = new ApprovedTraineeInformationForm();
        internship.setId(5);
        internship.setFillUserName(new Student());
        internship.getFillUserName().setUserName("student1");

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(10);
        companyBranch.setAddress("Test Address");
        internship.setCompanyBranch(companyBranch);

        internship.setHealthInsurance(true); // Eğer Boolean değilse: "Approved"
        internship.setInternshipStartDate(LocalDate.now());
        internship.setInternshipEndDate(LocalDate.now().plusDays(30));

        when(formRepository.findById(5)).thenReturn(Optional.of(internship));

        User user = new User();
        user.setUserName("student1");
        user.setEmail("student1@example.com");
        when(userRepository.findByUserName("student1")).thenReturn(user);

        // Act
        formService.approveInsurance(5, "studentAffairs");

        // Assert
        assertTrue(internship.getInsuranceApproval());
        assertNotNull(internship.getInsuranceApprovalDate());
        verify(formRepository).save(internship);
        verify(insuranceApprovalLogRepository).save(any(InsuranceApprovalLog.class));
        verify(emailService).sendEmail(eq("student1@example.com"), anyString(), anyString());
    }
}