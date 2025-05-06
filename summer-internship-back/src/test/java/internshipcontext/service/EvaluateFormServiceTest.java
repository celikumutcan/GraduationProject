package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.EvaluateFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EvaluateFormService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Unit test for EvaluateFormService.
 * Tests createEvaluationForm() method using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
public class EvaluateFormServiceTest {

    @Mock
    private EvaluateFormRepository evaluateFormRepository;

    @Mock
    private ApprovedTraineeInformationFormRepository traineeFormRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EvaluateFormService evaluateFormService;

    /**
     * Test createEvaluationForm() saves evaluation and sends email.
     */
    @Test
    public void testCreateEvaluationForm_SavesEvaluationAndSendsEmail() {
        // Arrange
        int formId = 1;

        // Create User
        User user = new User();
        user.setEmail("student@example.com");

        // Create Student and attach user
        Student student = new Student();
        student.setUsers(user); // Student -> User

        // Create form and attach student
        ApprovedTraineeInformationForm form = new ApprovedTraineeInformationForm();
        form.setFillUserName(student);

        when(traineeFormRepository.findById(formId)).thenReturn(Optional.of(form));

        // Act
        evaluateFormService.createEvaluationForm(
                formId,
                "Excellent",            // attendance
                "Good",                 // diligence and enthusiasm
                "Satisfactory",         // contribution to work environment
                "Excellent",             // overall performance
                "Very hardworking."      // comments
        );

        // Assert
        verify(evaluateFormRepository, times(1)).save(any(EvaluateForm.class));
        verify(emailService, times(1)).sendEmail(eq("student@example.com"), anyString(), anyString());
    }
}