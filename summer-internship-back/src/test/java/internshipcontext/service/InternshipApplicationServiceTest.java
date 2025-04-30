package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Unit test for applyForInternshipOffer() method in InternshipApplicationService.
 */
@ExtendWith(MockitoExtension.class)
public class InternshipApplicationServiceTest {

    @Mock
    private InternshipApplicationRepository internshipApplicationRepository;

    @Mock
    private InternshipOfferRepository internshipOfferRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private InternshipApplicationService internshipApplicationService;

    /**
     * Test applyForInternshipOffer() saves the application.
     */
    @Test
    public void testApplyForInternshipOffer_SavesApplication() {
        // Arrange
        String studentUsername = "student1";
        int offerId = 101;

        Student student = new Student();
        student.setUserName(studentUsername);

        InternshipOffer offer = new InternshipOffer();
        offer.setOfferId(offerId);
        when(studentRepository.findByUserName(studentUsername)).thenReturn(Optional.of(student));
        when(internshipOfferRepository.findById(offerId)).thenReturn(Optional.of(offer));

        // Act
        internshipApplicationService.applyForInternshipOffer(studentUsername, offerId);

        // Assert
        verify(internshipApplicationRepository, times(1))
                .save(any(InternshipApplication.class));
    }
}