package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipOfferRepository internshipOfferRepository;
    private final StudentRepository studentRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;

    // ✅ Constructor-based dependency injection
    public InternshipApplicationService(InternshipApplicationRepository internshipApplicationRepository,
                                        InternshipOfferRepository internshipOfferRepository,
                                        StudentRepository studentRepository,
                                        CompanyBranchRepository companyBranchRepository, ApprovedInternshipRepository approvedInternshipRepository, ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipOfferRepository = internshipOfferRepository;
        this.studentRepository = studentRepository;
        this.companyBranchRepository = companyBranchRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
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
     * 📌 Allows a student to apply for an internship offer.
     * @param studentUsername The username of the student applying.
     * @param internshipId The ID of the internship
     */
    public void applyForInternship(String studentUsername, Integer internshipId) {
        // Ensure student exists
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        // Ensure internship exists
        ApprovedTraineeInformationForm internship = approvedTraineeInformationFormRepository.findByid(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship with ID " + internshipId + " not found."));

        // Create and save the internship application
        InternshipApplication application = new InternshipApplication(student, internship);
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
}