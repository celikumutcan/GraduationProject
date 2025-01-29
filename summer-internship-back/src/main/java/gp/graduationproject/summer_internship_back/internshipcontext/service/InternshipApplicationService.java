package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipApplication;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InternshipApplicationRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final StudentRepository studentRepository;

    // ✅ Constructor-based dependency injection (Recommended)
    public InternshipApplicationService(InternshipApplicationRepository internshipApplicationRepository,
                                        CompanyBranchRepository companyBranchRepository,
                                        StudentRepository studentRepository) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.companyBranchRepository = companyBranchRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Submits an internship application for a student.
     * @param studentUsername The username of the student applying.
     * @param companyBranch The company branch where the student applies.
     * @param position The position applied for.
     * @return The created internship application.
     */
    public InternshipApplication applyForInternship(String studentUsername, CompanyBranch companyBranch, String position) {
        // Ensure student exists
        Student student = studentRepository.findByUserName(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        // Create and save the internship application
        InternshipApplication application = new InternshipApplication(student, companyBranch, position);
        return internshipApplicationRepository.save(application);
    }

    /**
     * Retrieves all internship applications submitted by a student.
     * @param studentUsername The username of the student.
     * @return List of internship applications.
     */
    public List<InternshipApplication> getStudentApplications(String studentUsername) {
        return internshipApplicationRepository.findByStudent_UserName(studentUsername);
    }

    /**
     * Retrieves all internship applications submitted to a specific company branch.
     * @param branchId The ID of the company branch.
     * @return List of internship applications.
     */
    public List<InternshipApplication> getCompanyApplications(Integer branchId) {
        // Ensure company branch exists
        CompanyBranch companyBranch = companyBranchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Company branch not found."));

        return internshipApplicationRepository.findByCompanyBranch_Id(branchId);
    }

    /**
     * Updates the status of an internship application.
     * @param applicationId The ID of the internship application.
     * @param newStatus The new status (e.g., "Approved" or "Rejected").
     */
    public void updateApplicationStatus(Long applicationId, String newStatus) {
        // Find the application by ID
        InternshipApplication application = internshipApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));

        // Update the status
        application.setStatus(newStatus);
        internshipApplicationRepository.save(application);
    }
}