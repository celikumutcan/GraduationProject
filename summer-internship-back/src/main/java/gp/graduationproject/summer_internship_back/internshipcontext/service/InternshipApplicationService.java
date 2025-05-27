package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@CacheConfig(cacheNames = "internshipApplications")
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipOfferRepository internshipOfferRepository;
    private final StudentRepository studentRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public InternshipApplicationService(
            InternshipApplicationRepository internshipApplicationRepository,
            InternshipOfferRepository internshipOfferRepository,
            StudentRepository studentRepository,
            CompanyBranchRepository companyBranchRepository,
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
            UserRepository userRepository,
            EmailService emailService
    ) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipOfferRepository = internshipOfferRepository;
        this.studentRepository = studentRepository;
        this.companyBranchRepository = companyBranchRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /**
     * Applies for an internship using minimal trainee form info.
     *
     * @param studentUsername The username of the student.
     * @param internshipId    The ID of the internship.
     */
    public void applyForInternship(String studentUsername, Integer internshipId) {
        Student student = new Student();
        student.setUserName(studentUsername);

        MinimalInternshipDTO minimalInfo = approvedTraineeInformationFormRepository
                .findMinimalInternshipDTOById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found."));

        InternshipApplication application = new InternshipApplication(
                student,
                minimalInfo.getCompanyBranch(),
                minimalInfo.getPosition()
        );

        internshipApplicationRepository.save(application);
    }

    /**
     * Returns all internship applications for a given offer.
     *
     * @param offerId ID of the offer.
     * @return List of applications.
     */
    public List<InternshipApplication> getApplicationsForOffer(Integer offerId) {
        InternshipOffer internshipOffer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found."));
        return internshipApplicationRepository.findByInternshipOffer(internshipOffer);
    }

    /**
     * Retrieves internship applications of a student using lightweight DTOs.
     *
     * @param username The username of the student
     * @return List of StudentInternshipApplicationSimpleDTO
     */
    public List<StudentInternshipApplicationSimpleDTO> getStudentApplications(String username) {
        return internshipApplicationRepository.findStudentApplicationsOptimized(username);
    }


    /**
     * Updates the status of an internship application.
     *
     * @param applicationId ID of the application.
     * @param status        New status.
     */
    public void updateApplicationStatus(Long applicationId, String status) {
        InternshipApplication application = internshipApplicationRepository.findByIdWithStudentAndUser(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));

        application.setStatus(status);
        internshipApplicationRepository.save(application);

        String studentEmail = application.getStudent().getUsers().getEmail();
        String studentUsername = application.getStudent().getUserName();
        String position = application.getPosition();

        emailService.sendApplicationStatusEmail(studentEmail, studentUsername, position, status);
    }


    /**
     * Returns student internship applications as lightweight DTOs.
     *
     * @param studentUsername The username of the student.
     * @return List of optimized DTOs.
     */
    public List<StudentInternshipApplicationSimpleDTO> getStudentApplicationsAsDTO(String studentUsername) {
        return internshipApplicationRepository.findStudentApplicationsOptimized(studentUsername);
    }


    /**
     * Checks whether a student has applied to a specific internship offer.
     *
     * @param studentUserName The student's username.
     * @param offerId         The offer ID.
     * @return True if applied, false otherwise.
     */
    @Cacheable(key = "#studentUserName + '-' + #offerId")
    public boolean hasStudentAppliedForOffer(String studentUserName, Integer offerId) {
        return internshipApplicationRepository.existsByStudentUserName_UserNameAndInternshipOffer_OfferId(studentUserName, offerId);
    }

    /**
     * Applies for a given internship offer and sends email notification to the company.
     *
     * @param studentUsername The student applying.
     * @param offerId         The offer being applied for.
     * @return Saved InternshipApplication object.
     */
    public InternshipApplication applyForInternshipOffer(String studentUsername, Integer offerId) {
        StudentUsernameDTO studentDTO = studentRepository.findUsernameOnlyByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        Student student = new Student();
        student.setUserName(studentDTO.getUserName());

        InternshipOfferBasicDTO offer = internshipOfferRepository.findBasicInfoById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found."));

        InternshipOffer internshipOffer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found."));

        InternshipApplication application = new InternshipApplication();
        application.setStudent(student);
        application.setCompanyBranch(offer.getCompanyBranch());
        application.setPosition(offer.getPosition());
        application.setStatus("Waiting");
        application.setApplicationDate(Instant.now());
        application.setInternshipOffer(internshipOffer);

        InternshipApplication savedApplication = internshipApplicationRepository.save(application);

        emailService.sendApplicationNotificationToCompanyBranchSimple(
                student.getUserName(),
                offer.getPosition(),
                offer.getCompanyBranch().getBranchEmail()
        );

        return savedApplication;
    }

    /**
     * Returns a list of DTOs for applicants (with CVs) to a specific offer.
     *
     * @param offerId The internship offer ID.
     * @return List of CompanyOfferApplicationViewDTO.
     */
    public List<CompanyOfferApplicationViewDTO> getApplicationsWithCVForOffer(Integer offerId) {
        return internshipApplicationRepository.getAllApplicantsWithCV(offerId);
    }

    /**
     * Returns a list of DTOs for applicants (general use)
     *
     * @param branchId The branch ID
     * @return List of CompanyRegularApplicationViewDTO.
     */
    public List<CompanyRegularApplicationViewDTO> getAllRegularApplicantsWithCV(Integer branchId) {
        return internshipApplicationRepository.getAllRegularApplicantsWithCV(branchId);
    }

}