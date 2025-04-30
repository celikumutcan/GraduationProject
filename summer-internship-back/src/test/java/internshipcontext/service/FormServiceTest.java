package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Form;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.FormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.FormService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for FormService.
 * Tests the addForm() method with mocked repositories.
 */
@ExtendWith(MockitoExtension.class)
public class FormServiceTest {

    @Mock
    private FormRepository formRepository;

    @Mock
    private AcademicStaffRepository academicStaffRepository;

    @InjectMocks
    private FormService formService;

    /**
     * TC8: Tests if addForm() saves form and returns non-null result.
     */
    @Test
    public void testAddForm_SavesAndReturnsForm() {
        // Arrange
        String file = "file.pdf";
        String content = "This is the form content.";
        String username = "academic1";

        AcademicStaff academicStaff = new AcademicStaff();
        academicStaff.setUserName(username);

        Form savedForm = new Form();
        savedForm.setId(1);

        when(academicStaffRepository.findById(username)).thenReturn(Optional.of(academicStaff));
        when(formRepository.save(any(Form.class))).thenReturn(savedForm);

        // Act
        Form result = formService.addForm(file, content, username);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(formRepository, times(1)).save(any(Form.class));
    }
}