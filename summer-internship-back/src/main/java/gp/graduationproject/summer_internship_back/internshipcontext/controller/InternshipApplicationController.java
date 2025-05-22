package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.BrowseInternshipApplicationDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipApplicationDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.MinimalInternshipDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.OfferMailInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
     * Sends email notifications to both the company branch and the student
     * after an internship application is submitted.
     *
     * @param studentUserName the username of the student who applied
     * @param internshipID the ID of the approved internship form
     */
    private void sendApprovalNotification(String studentUserName, Integer internshipID) {
        System.out.println("Approval notification triggered for: " + studentUserName);

        // Get student user from the repository
        User student = userRepository.findByUserName(studentUserName);
        if (student == null || student.getEmail() == null || student.getEmail().isBlank()) {
            System.out.println("Student Not Found!");
            return;
        }
        String studentEmail = student.getEmail();

        // Get minimal info for the approved internship form
        Optional<MinimalInternshipDTO> minimalOpt = approvedTraineeInformationFormService.getMinimalApprovedTraineeInformationFormById(internshipID);
        if (minimalOpt.isEmpty()) {
            System.out.println("Approved Internship Form Not Found!");
            return;
        }

        MinimalInternshipDTO minimal = minimalOpt.get();
        CompanyBranch companyBranch = minimal.getCompanyBranch();
        if (companyBranch == null || companyBranch.getBranchEmail() == null) {
            System.out.println("Company Branch Not Found!");
            return;
        }

        // Get the company representative by email
        Optional<User> companyRepresentativeOpt = userRepository.findByEmailAndUserType(companyBranch.getBranchEmail(), "company_branch");
        if (companyRepresentativeOpt.isEmpty()) {
            System.out.println("Company Representative Not Found for email: " + companyBranch.getBranchEmail());
            return;
        }

        User companyRepresentative = companyRepresentativeOpt.get();
        String companyEmail = companyRepresentative.getEmail();
        String companyName = companyBranch.getBranchName();

        // Send email to the company representative
        String companySubject = "New Internship Application Requires Approval";
        String companyBody = "Dear " + companyName + ",\n\n" +
                "A new internship application has been received and is waiting for your approval.\n\n" +
                "Student Name: " + student.getUserName() + "\n" +
                "Position: " + minimal.getPosition() + "\n\n" +
                "Please review it at your earliest convenience.\n\n" +
                "Best regards,\nInternship Management System";

        emailService.sendEmail(companyEmail, companySubject, companyBody);

        // Send confirmation email to the student
        String studentSubject = "Your Internship Application Sent to Company";
        String studentBody = "Dear " + student.getUserName() + ",\n\n" +
                "Your internship form application has been sent to the company. Please wait for the company’s approval.\n" +
                "It is now waiting for company approval.\n\n" +
                "Best regards,\nInternship Management System";

        emailService.sendEmail(studentEmail, studentSubject, studentBody);
    }


    /**
     * 📌 Retrieves all applications for a specific company branch.
     * @param branchId The ID of the company branch.
     * @return List of applications submitted to the branch.
     */
    @GetMapping("/company/{branchId}")
    public ResponseEntity<List<InternshipApplication>> getCompanyApplications(@PathVariable Integer branchId)
    {
        List<InternshipApplication> applications = internshipApplicationService.getCompanyApplications(branchId);
        return ResponseEntity.ok(applications);
    }

    /**
     * Approves a specific internship application.
     *
     * @param applicationId The ID of the application to approve.
     * @return Success message.
     */
    @PutMapping("/approve/{applicationId}")
    public ResponseEntity<String> approveApplication(@PathVariable Integer applicationId) {
        internshipApplicationService.updateApplicationStatus(applicationId, "Approved");
        return ResponseEntity.ok("Application approved.");
    }

    /**
     * Rejects a specific internship application.
     *
     * @param applicationId The ID of the application to reject.
     * @return Success message.
     */
    @PutMapping("/reject/{applicationId}")
    public ResponseEntity<String> rejectApplication(@PathVariable Integer applicationId) {
        internshipApplicationService.updateApplicationStatus(applicationId, "Rejected");
        return ResponseEntity.ok("Application rejected.");
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

}