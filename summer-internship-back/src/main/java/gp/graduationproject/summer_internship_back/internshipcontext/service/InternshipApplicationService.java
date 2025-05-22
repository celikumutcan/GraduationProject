package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipApplicationDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.MinimalInternshipDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipOfferRepository internshipOfferRepository;
    private final StudentRepository studentRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for injecting dependencies.
     */
    public InternshipApplicationService(InternshipApplicationRepository internshipApplicationRepository,
                                        InternshipOfferRepository internshipOfferRepository,
                                        StudentRepository studentRepository,
                                        CompanyBranchRepository companyBranchRepository,
                                        ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
                                        UserRepository userRepository) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipOfferRepository = internshipOfferRepository;
        this.studentRepository = studentRepository;
        this.companyBranchRepository = companyBranchRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.userRepository = userRepository;
    }

    /**
     * Applies for an internship using minimal fields.
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
     * Retrieves applications for a specific internship offer.
     */
    public List<InternshipApplication> getApplicationsForOffer(Integer offerId) {
        InternshipOffer internshipOffer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found."));
        return internshipApplicationRepository.findByInternshipOffer(internshipOffer);
    }

    /**
     * Retrieves all applications submitted by a student.
     */
    public List<InternshipApplication> getStudentApplications(String studentUsername) {
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));
        return internshipApplicationRepository.findByStudent(student);
    }

    /**
     * Retrieves all applications submitted to a specific company branch.
     */
    public List<InternshipApplication> getCompanyApplications(Integer branchId) {
        CompanyBranch companyBranch = companyBranchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Company branch not found."));
        return internshipApplicationRepository.findByCompanyBranch(companyBranch);
    }

    /**
     * Retrieves an application by its ID.
     */
    public InternshipApplication getApplicationById(Integer internshipID) {
        return internshipApplicationRepository.findById(internshipID).orElse(null);
    }

    /**
     * Updates the status of an internship application.
     */
    public void updateApplicationStatus(Integer applicationId, String status) {
        InternshipApplication application = internshipApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));
        application.setStatus(status);
        internshipApplicationRepository.save(application);
    }

    /**
     * Returns student applications as DTOs.
     */
    public List<InternshipApplicationDTO> getStudentApplicationsAsDTO(String studentUsername) {
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        List<InternshipApplication> applications = internshipApplicationRepository.findAllByStudentUserNameWithBranch(studentUsername);

        return applications.stream().map(app -> new InternshipApplicationDTO(
                app.getApplicationId(),
                app.getStudent().getUserName(),
                app.getCompanyBranch() != null ? app.getCompanyBranch().getBranchName() : null,
                app.getPosition(),
                app.getApplicationDate(),
                app.getStatus(),
                app.getInternshipOffer() != null ? app.getInternshipOffer().getOfferId().longValue() : null
        )).toList();
    }

    /**
     * Checks if a student has already applied for an offer.
     */
    public boolean hasStudentAppliedForOffer(String studentUserName, Integer offerId) {
        return internshipApplicationRepository.existsByStudentUserName_UserNameAndInternshipOffer_OfferId(studentUserName, offerId);
    }

    /**
     * Applies for an internship offer.
     */
    public void applyForInternshipOffer(String studentUsername, Integer offerId) {
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        InternshipOffer offer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found"));

        InternshipApplication application = new InternshipApplication();
        application.setStudent(student);
        application.setInternshipOffer(offer);
        application.setCompanyBranch(offer.getCompanyBranch());
        application.setPosition(offer.getPosition());
        application.setStatus("Pending");
        application.setApplicationDate(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        internshipApplicationRepository.save(application);
    }
}