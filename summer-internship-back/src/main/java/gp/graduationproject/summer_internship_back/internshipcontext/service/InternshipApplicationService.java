package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipApplicationDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.MinimalInternshipDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipOfferRepository internshipOfferRepository;
    private final StudentRepository studentRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final UserRepository userRepository;

    // ✅ Constructor-based dependency injection
    public InternshipApplicationService(InternshipApplicationRepository internshipApplicationRepository,
                                        InternshipOfferRepository internshipOfferRepository,
                                        StudentRepository studentRepository,
                                        CompanyBranchRepository companyBranchRepository, ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,UserRepository userRepository) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipOfferRepository = internshipOfferRepository;
        this.studentRepository = studentRepository;
        this.companyBranchRepository = companyBranchRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.userRepository = userRepository;
    }

    /**
     * 📌 Allows a student to apply for an internship offer.
     * @param studentUsername The username of the student applying.
     * @param offerId The ID of the internship offer.
     */
    public void applyForInternshipOffer(String studentUsername, Integer offerId) {
        // Ensure student exists
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        // Ensure internship offer exists
        InternshipOffer internshipOffer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found."));

        // Create and save the internship application
        InternshipApplication application = new InternshipApplication(student, internshipOffer);
        internshipApplicationRepository.save(application);
    }

    /**
     * Applies for an internship by loading only necessary fields to improve performance.
     *
     * @param studentUsername The username of the student applying.
     * @param internshipId The ID of the internship.
     */
    public void applyForInternship(String studentUsername, Integer internshipId) {
        Student student = new Student();
        student.setUserName(studentUsername); // Sadece username yeter

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
     * 📌 Retrieves all internship applications for a specific internship offer.
     * @param offerId The ID of the internship offer.
     * @return List of internship applications for the offer.
     */
    public List<InternshipApplication> getApplicationsForOffer(Integer offerId) {
        InternshipOffer internshipOffer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found."));
        return internshipApplicationRepository.findByInternshipOffer(internshipOffer);
    }

    /**
     * 📌 Retrieves all applications submitted by a specific student.
     * @param studentUsername The username of the student.
     * @return List of applications made by the student.
     */
    public List<InternshipApplication> getStudentApplications(String studentUsername) {
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));
        return internshipApplicationRepository.findByStudent(student);
    }

    /**
     * 📌 Retrieves all applications for a specific company branch.
     * @param branchId The ID of the company branch.
     * @return List of applications submitted to the branch.
     */
    public List<InternshipApplication> getCompanyApplications(Integer branchId) {
        CompanyBranch companyBranch = companyBranchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Company branch not found."));
        return internshipApplicationRepository.findByCompanyBranch(companyBranch);
    }
    public InternshipApplication getApplicationById(Integer internshipID) {
        return internshipApplicationRepository.findById(internshipID)
                .orElse(null); // Eğer başvuru bulunamazsa null döndür
    }

    public void updateApplicationStatus(Integer applicationId, String status) {
        InternshipApplication application = internshipApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));
        application.setStatus(status);
        internshipApplicationRepository.save(application);
    }


    /**
     * Returns internship applications as DTOs for a specific student.
     * @param studentUsername Username of the student
     * @return List of InternshipApplicationDTO
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
}