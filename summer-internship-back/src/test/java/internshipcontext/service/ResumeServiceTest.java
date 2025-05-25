package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ResumeRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for the ResumeService.addResume() method.
 */
@ExtendWith(MockitoExtension.class)
public class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ResumeService resumeService;

    /**
     * Tests that addResume() successfully saves a resume when a valid student and file are provided.
     */
    @Test
    public void testAddResume_SuccessfullySavesResume() {
        String username = "student1";
        String fileContent = "This is the resume content.";

        Student student = new Student();
        student.setUserName(username);

        when(studentRepository.findByUserName(username)).thenReturn(Optional.of(student));
        when(resumeRepository.save(any(Resume.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Resume savedResume = resumeService.addResume(fileContent, username);

        assertNotNull(savedResume);
        assertEquals(fileContent, savedResume.getFileName());
        assertEquals(username, savedResume.getUserName());
        verify(resumeRepository).save(any(Resume.class));
    }
}