package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipApplicationService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferBasicDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.StudentUsernameDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Unit test for the applyForInternshipOffer() method in InternshipApplicationService.
 */
@ExtendWith(MockitoExtension.class)
public class InternshipApplicationServiceTest {

    @Mock
    private InternshipApplicationRepository internshipApplicationRepository;

    @Mock
    private InternshipOfferRepository internshipOfferRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private InternshipApplicationService internshipApplicationService;

    /**
     * Tests that applyForInternshipOffer() saves the internship application successfully.
     */
    @Test
    public void testApplyForInternshipOffer_SavesApplication() {
        String studentUsername = "student1";
        int offerId = 101;

        StudentUsernameDTO studentDTO = mock(StudentUsernameDTO.class);
        when(studentDTO.getUserName()).thenReturn(studentUsername);

        CompanyBranch branch = new CompanyBranch();
        branch.setBranchEmail("test@company.com");

        InternshipOfferBasicDTO offerDTO = mock(InternshipOfferBasicDTO.class);
        when(offerDTO.getPosition()).thenReturn("Software Intern");
        when(offerDTO.getCompanyBranch()).thenReturn(branch);

        InternshipOffer internshipOffer = new InternshipOffer();
        internshipOffer.setOfferId(offerId);

        when(studentRepository.findUsernameOnlyByUserName(studentUsername)).thenReturn(Optional.of(studentDTO));
        when(internshipOfferRepository.findBasicInfoById(offerId)).thenReturn(Optional.of(offerDTO));
        when(internshipOfferRepository.findById(offerId)).thenReturn(Optional.of(internshipOffer));

        internshipApplicationService.applyForInternshipOffer(studentUsername, offerId);

        verify(internshipApplicationRepository, times(1)).save(any(InternshipApplication.class));
    }
}
