package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipApplication;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipApplicationService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipApplicationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to manage internship applications.
 */
@RestController
@RequestMapping("/api/internship-applications")
public class InternshipApplicationController {

    private final InternshipApplicationService internshipApplicationService;
    private final CompanyBranchService companyBranchService;

    public InternshipApplicationController(InternshipApplicationService internshipApplicationService, CompanyBranchService companyBranchService)
    {
        this.internshipApplicationService = internshipApplicationService;
        this.companyBranchService = companyBranchService;
    }

    /**
     * Allows a student to apply for an internship.
     * @param studentUsername The username of the student.
     * @param branchId The ID of the company branch.
     * @param position The position for which the student is applying.
     * @return Response indicating the application status.
     */
    @PostMapping("/apply")
    public ResponseEntity<String> applyForInternship(
            @RequestParam String studentUsername,
            @RequestParam Integer branchId,
            @RequestParam String position
    ) {
        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(branchId)
                .orElseThrow(() -> new RuntimeException("Company branch not found."));

        internshipApplicationService.applyForInternship(studentUsername, companyBranch, position);
        return ResponseEntity.ok("Internship application submitted successfully.");
    }

    /**
     * Retrieves all internship applications submitted by a specific student.
     * @param studentUsername The username of the student.
     * @return List of internship applications.
     */
    @GetMapping("/student")
    public ResponseEntity<List<InternshipApplicationDTO>> getStudentApplications(@RequestParam String studentUsername) {
        List<InternshipApplication> applications = internshipApplicationService.getStudentApplications(studentUsername);

        List<InternshipApplicationDTO> applicationDTOs = applications.stream()
                .map(app -> new InternshipApplicationDTO(
                        app.getApplicationId(),
                        app.getStudent().getUserName(),
                        app.getCompanyBranch().getBranchName(),
                        app.getPosition(),
                        app.getApplicationDate(),
                        app.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(applicationDTOs);
    }

    /**
     * Retrieves all internship applications submitted to a specific company branch.
     * @param branchId The ID of the company branch.
     * @return List of internship applications.
     */
    @GetMapping("/company")
    public ResponseEntity<List<InternshipApplicationDTO>> getCompanyApplications(@RequestParam Integer branchId) {
        List<InternshipApplication> applications = internshipApplicationService.getCompanyApplications(branchId);

        List<InternshipApplicationDTO> applicationDTOs = applications.stream()
                .map(app -> new InternshipApplicationDTO(
                        app.getApplicationId(),
                        app.getStudent().getUserName(),
                        app.getCompanyBranch().getBranchName(),
                        app.getPosition(),
                        app.getApplicationDate(),
                        app.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(applicationDTOs);
    }

    /**
     * Updates the status of an internship application.
     * @param applicationId The ID of the application.
     * @param newStatus The new status (Approved/Rejected).
     * @return Response indicating the update status.
     */
    @PatchMapping("/update-status")
    public ResponseEntity<String> updateApplicationStatus(
            @RequestParam Long applicationId,
            @RequestParam String newStatus
    ) {
        internshipApplicationService.updateApplicationStatus(applicationId, newStatus);
        return ResponseEntity.ok("Internship application status updated successfully.");
    }
}