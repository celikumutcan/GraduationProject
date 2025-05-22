package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller to manage internship applications.
 */
@RestController
@RequestMapping("/api/internship-applications")
public class InternshipApplicationController {

    private final InternshipApplicationService internshipApplicationService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final CompanyBranch companyBranch;
    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;
    private final InternshipOfferService internshipOfferService;

    public InternshipApplicationController(
            InternshipApplicationService internshipApplicationService,
            UserService userService,
            EmailService emailService,
            UserRepository userRepository,
            ApprovedTraineeInformationFormService approvedTraineeInformationFormService,
            InternshipOfferService internshipOfferService // ➕ eklenen
    ) {
        this.internshipApplicationService = internshipApplicationService;
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
        this.internshipOfferService = internshipOfferService;
        this.companyBranch = new CompanyBranch();
    }

    /**
     * Allows a student to apply for an internship from Browse Internship.
     * @param studentUsername The username of the student.
     * @param internshipID The ID of the internship.
     * @return Response indicating the application status.
     */
    @PostMapping("/applyForInternship")
    public ResponseEntity<String> applyForInternship(@RequestParam String studentUsername, @RequestParam Integer internshipID) {

        internshipApplicationService.applyForInternship(studentUsername, internshipID);
        emailService.sendApprovalNotificationAsync(studentUsername, internshipID, userRepository, approvedTraineeInformationFormService);
        return ResponseEntity.ok("Internship application for the offer submitted successfully.");
    }

    @PostMapping("/applyForOffer")
    public ResponseEntity<String> applyForInternshipOffer(@RequestParam String studentUsername,
                                                          @RequestParam Integer offerId) {
        internshipApplicationService.applyForInternshipOffer(studentUsername, offerId);
        return ResponseEntity.ok("Application submitted successfully");
    }


    /**
     * 📌 Retrieves all applications for a specific internship offer.
     * @param offerId The ID of the internship offer.
     * @return List of applications for the offer.
     */
    @GetMapping("/offer/{offerId}")
    public ResponseEntity<List<InternshipApplication>> getApplicationsForOffer(@PathVariable Integer offerId)
    {
        List<InternshipApplication> applications = internshipApplicationService.getApplicationsForOffer(offerId);
        return ResponseEntity.ok(applications);
    }

    /**
     * 📌 Retrieves all applications submitted by a specific student.
     * @param studentUsername The username of the student.
     * @return List of applications made by the student.
     */
    @GetMapping("/student/{studentUsername}")
    public ResponseEntity<List<BrowseInternshipApplicationDTO>> getStudentApplications(@PathVariable String studentUsername)
    {


        // Fetch applications from the service
        List<InternshipApplication> applications = internshipApplicationService.getStudentApplications(studentUsername);

        // Map the list of InternshipApplication to BrowseInternshipApplicationDTO in a single step
        List<BrowseInternshipApplicationDTO> applicationDTOs = applications.stream()
                .map(application -> new BrowseInternshipApplicationDTO(application.getPosition(),application.getCompanyBranch().getId()))
                .collect(Collectors.toList());

        // Return the mapped list of DTOs in the response
        return ResponseEntity.ok(applicationDTOs);
    }


    /**
     * Retrieves all applications for a specific company branch.
     * @param branchId The ID of the company branch.
     * @return List of applications submitted to the branch.
     */
    @GetMapping("/company/{branchId}")
    public ResponseEntity<List<InternshipApplicationDTO>> getCompanyApplications(@PathVariable Integer branchId)
    {
        List<InternshipApplicationDTO> applications = internshipApplicationService.getCompanyApplications(branchId);
        return ResponseEntity.ok(applications);
    }

    /**
     * Retrieves all applications for a specific company branch.
     * @param branchId The ID of the company branch.
     * @return List of applications submitted to the branch.
     */
    @GetMapping("/company/{branchId}/with-cv")
    public ResponseEntity<List<CompanyRegularApplicationViewDTO>> getCompanyApplicationsWithCV(@PathVariable Integer branchId)
    {
        List<CompanyRegularApplicationViewDTO> applicants = internshipApplicationService.getAllRegularApplicantsWithCV(branchId);
        return ResponseEntity.ok(applicants);
    }


    /**
     * Approves a specific internship application.
     *
     * @param applicationId The ID of the application to approve.
     * @return Success message.
     */
    @PutMapping("/approve/{applicationId}")
    public ResponseEntity<Map<String, String>> approveApplication(@PathVariable Long applicationId) {
        internshipApplicationService.updateApplicationStatus(applicationId, "Approved");
        return ResponseEntity.ok(Map.of("message", "Application approved."));
    }

    /**
     * Rejects a specific internship application.
     *
     * @param applicationId The ID of the application to reject.
     * @return Success message.
     */
    @PutMapping("/reject/{applicationId}")
    public ResponseEntity<Map<String, String>> rejectApplication(@PathVariable Long applicationId) {
        internshipApplicationService.updateApplicationStatus(applicationId, "Rejected");
        return ResponseEntity.ok(Map.of("message", "Application rejected."));
    }



    /**
     * Returns internship applications of the student as DTO.
     * @param username The username of the student
     * @return List of InternshipApplicationDTO
     */
    @GetMapping("/student-dto/{username}")
    public List<InternshipApplicationDTO> getStudentApplicationsDTO(@PathVariable String username) {
        return internshipApplicationService.getStudentApplicationsAsDTO(username);
    }



    @GetMapping("/has-applied")
    public ResponseEntity<Boolean> hasApplied(
            @RequestParam String studentUsername,
            @RequestParam Integer offerId) {
        boolean applied = internshipApplicationService.hasStudentAppliedForOffer(studentUsername, offerId);
        return ResponseEntity.ok(applied);
    }


    /**
     * Returns all student applications with CVs for an internship offer.
     *
     * @param offerId the internship offer ID
     * @return list of students with CV info
     */
    @GetMapping("/offer/{offerId}/with-cv")
    public ResponseEntity<List<CompanyOfferApplicationViewDTO>> getOfferApplicationsWithCV(@PathVariable Integer offerId) {
        List<CompanyOfferApplicationViewDTO> applications = internshipApplicationService.getApplicationsWithCVForOffer(offerId);
        System.out.println("DTO count = " + applications.size());
        for (CompanyOfferApplicationViewDTO dto : applications) {
            System.out.println(dto.getUserName() + " - " + dto.getFullName());
        }
        return ResponseEntity.ok(applications);
    }

}