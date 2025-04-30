package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for ReportService.addReport() method.
 */
@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ApprovedTraineeInformationFormRepository formRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportService reportService;

    /**
     * Test addReport() method with valid data.
     */
    @Test
    public void testAddReport_SavesReportAndSendsEmailOnSecondUpload() throws Exception {
        // Arrange
        ReportDTO dto = new ReportDTO();
        dto.setTraineeInformationFormId(1);
        dto.setUserName("student1");
        dto.setFile(new MockMultipartFile("file", "report.pdf", "application/pdf", "Test content".getBytes()));

        User studentUser = new User();
        studentUser.setUserName("student1");

        Student student = new Student();
        student.setUserName("student1");
        student.setUsers(studentUser);

        ApprovedTraineeInformationForm form = new ApprovedTraineeInformationForm();
        form.setId(1);
        form.setFillUserName(student);
        form.setStatus("Approved");
        form.setEvaluatingFacultyMember("instructor1");

        User instructor = new User();
        instructor.setUserName("instructor1");
        instructor.setEmail("instructor@example.com");

        Report existingReport = new Report();

        when(formRepository.findById(1)).thenReturn(Optional.of(form));
        when(reportRepository.findAllByTraineeInformationForm_Id(1)).thenReturn(Collections.singletonList(existingReport));
        when(reportRepository.save(any(Report.class))).thenAnswer(i -> i.getArgument(0));
        when(userRepository.findByUserName("instructor1")).thenReturn(instructor);

        // Act
        Report saved = reportService.addReport(dto);

        // Assert
        assertNotNull(saved);
        verify(reportRepository).save(any(Report.class));
        verify(emailService).sendEmail(eq("instructor@example.com"), anyString(), contains("revised report"));
    }
}