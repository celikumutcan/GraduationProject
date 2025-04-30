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
 * Unit test for createAllEvaluations() method in ReportEvaluationService.
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
     * Test createAllEvaluations() saves evaluations and sends email.
     */
    @Test
    public void testCreateAllEvaluations_SavesAndSendsEmail() {
        // Arrange
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

        Report report = new Report();
        User student = new User();
        student.setUserName("student1");
        student.setEmail("student@example.com");

        when(reportRepository.findById(1)).thenReturn(Optional.of(report));
        when(userRepository.findByUserName("student1")).thenReturn(student);

        // Act
        evaluationService.createAllEvaluations(dto);

        // Assert
        verify(evaluationRepository, times(11)).save(any(ReportEvaluation.class));
        verify(emailService, times(1)).sendEmail(eq("student@example.com"), anyString(), contains("evaluated"));
    }
}