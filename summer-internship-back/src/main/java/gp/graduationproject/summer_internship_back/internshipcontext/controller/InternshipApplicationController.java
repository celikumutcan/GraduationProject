package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipApplication;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipApplicationService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.BrowseInternshipApplicationDTO;
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

    public InternshipApplicationController(InternshipApplicationService internshipApplicationService)
    {
        this.internshipApplicationService = internshipApplicationService;
    }

    /**
     * 📌 Allows a student to apply for an internship from Browse Internship.
     * @param studentUsername The username of the student.
     * @param internshipID The ID of the internship.
     * @return Response indicating the application status.
     */
    @PostMapping("/applyForInternship")
    public ResponseEntity<String> applyForInternship(@RequestParam String studentUsername, @RequestParam Integer internshipID)
    {
        internshipApplicationService.applyForInternship(studentUsername, internshipID);
        return ResponseEntity.ok("Internship application for the offer submitted successfully.");
    }


    /**
     * 📌 Allows a student to apply for an internship offer.
     * @param studentUsername The username of the student.
     * @param offerId The ID of the internship offer.
     * @return Response indicating the application status.
     */
    @PostMapping("/applyForOffer")
    public ResponseEntity<String> applyForInternshipOffer(@RequestParam String studentUsername, @RequestParam Integer offerId)
    {
        internshipApplicationService.applyForInternshipOffer(studentUsername, offerId);
        return ResponseEntity.ok("Internship application for the offer submitted successfully.");
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
}