package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportEvaluationService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportEvaluationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Unit test for the createAllEvaluations() method in ReportEvaluationService.
 */
@ExtendWith(MockitoExtension.class)
public class ReportEvaluationServiceTest {

    @Mock
    private ReportEvaluationRepository evaluationRepository;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportEvaluationService evaluationService;

    /**
     * Tests that createAllEvaluations() saves all evaluation items and sends an email to the student.
     */
    @Test
    public void testCreateAllEvaluations_SavesAndSendsEmail() {
        ReportEvaluationDTO dto = new ReportEvaluationDTO();
        dto.setReportId(1);
        dto.setStudentUserName("student1");

        dto.setCompanyEvalGrade(5.0);
        dto.setCompanyEvalComment("Good");
        dto.setReportStructureGrade(10.0);
        dto.setReportStructureComment("Well structured");
        dto.setAbstractGrade(5.0);
        dto.setAbstractComment("Clear");
        dto.setProblemStatementGrade(5.0);
        dto.setProblemStatementComment("Relevant");
        dto.setIntroductionGrade(5.0);
        dto.setIntroductionComment("Informative");
        dto.setTheoryGrade(10.0);
        dto.setTheoryComment("Strong base");
        dto.setAnalysisGrade(10.0);
        dto.setAnalysisComment("Detailed");
        dto.setModellingGrade(15.0);
        dto.setModellingComment("Accurate");
        dto.setProgrammingGrade(20.0);
        dto.setProgrammingComment("Excellent");
        dto.setTestingGrade(10.0);
        dto.setTestingComment("Good coverage");
        dto.setConclusionGrade(5.0);
        dto.setConclusionComment("Well summarized");
        dto.setFeedback("Well done");

        Report report = new Report();
        report.setId(1);

        ApprovedTraineeInformationForm traineeForm = new ApprovedTraineeInformationForm();
        Student student = new Student();
        User user = new User();
        user.setUserName("student1");
        user.setEmail("student@example.com");
        student.setUsers(user);
        traineeForm.setFillUserName(student);
        report.setTraineeInformationForm(traineeForm);

        when(reportRepository.findReportWithStudent(1)).thenReturn(Optional.of(report));
        when(evaluationRepository.save(any(ReportEvaluation.class))).thenReturn(new ReportEvaluation());

        evaluationService.createAllEvaluations(dto);

        verify(evaluationRepository, times(11)).save(any(ReportEvaluation.class));
        verify(emailService).sendEmail(eq("student@example.com"), anyString(), contains("evaluated"));
    }
}
