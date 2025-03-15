package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipOfferService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling internship offers.
 */
@RestController
@RequestMapping("/api/internship-offers")
public class InternshipOfferController {

    private final InternshipOfferService internshipOfferService;

    /**
     * Constructor-based dependency injection.
     */
    public InternshipOfferController(InternshipOfferService internshipOfferService) {
        this.internshipOfferService = internshipOfferService;
    }

    /**
     * Creates a new internship offer.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createInternshipOffer(
            @RequestParam Integer branchId,
            @RequestParam String position,
            @RequestParam String department,
            @RequestParam String details,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        internshipOfferService.createInternshipOffer(branchId, position, department, details, startDate, endDate);
        return ResponseEntity.ok("Internship offer created successfully.");
    }

    /**
     * Retrieves all open internship offers.
     */
    @GetMapping("/open")
    public ResponseEntity<List<InternshipOffer>> getAllOpenInternshipOffers() {
        List<InternshipOffer> offers = internshipOfferService.getAllOpenInternshipOffers();
        return ResponseEntity.ok(offers);
    }

    /**
     * Retrieves all internship offers from a specific company.
     */
    @GetMapping("/company/{userName}")
    public ResponseEntity<List<InternshipOffer>> getCompanyInternshipOffers(@PathVariable String userName) {
        List<InternshipOffer> offers = internshipOfferService.getCompanyInternshipOffers(userName);
        return ResponseEntity.ok(offers);
    }

    /**
     * Updates an internship offer.
     */
    @PutMapping("/update/{offerId}")
    public ResponseEntity<String> updateInternshipOffer(
            @PathVariable Integer offerId,
            @RequestParam String position,
            @RequestParam String department,
            @RequestParam String details,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String status) {

        internshipOfferService.updateInternshipOffer(offerId, position, department, details, startDate, endDate, status);
        return ResponseEntity.ok("Internship offer updated successfully.");
    }

    /**
     * Deletes an internship offer.
     */
    @DeleteMapping("/delete/{offerId}")
    public ResponseEntity<String> deleteInternshipOffer(@PathVariable Integer offerId) {
        internshipOfferService.deleteInternshipOffer(offerId);
        return ResponseEntity.ok("Internship offer deleted successfully.");
    }

    @Autowired
    private ResumeRecommendationService resumeRecommendationService;

    @GetMapping("/recommended/{username}")
    public ResponseEntity<List<InternshipOffer>> getRecommendedInternships(@PathVariable String username) {
        List<InternshipOffer> recommendations = resumeRecommendationService.recommendInternships(username);
        return ResponseEntity.ok(recommendations);
    }
}