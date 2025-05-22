package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipOfferService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferCreateDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internship-offers")
public class InternshipOfferController {

    private final InternshipOfferService internshipOfferService;

    public InternshipOfferController(InternshipOfferService internshipOfferService) {
        this.internshipOfferService = internshipOfferService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createInternshipOffer(@RequestBody InternshipOfferCreateDTO dto) {
        internshipOfferService.createInternshipOffer(dto);
        return ResponseEntity.ok(Map.of("message", "Internship offer created successfully."));
    }

    @GetMapping("/open")
    public ResponseEntity<List<InternshipOfferListDTO>> getAllOpenInternshipOffers() {
        List<InternshipOfferListDTO> offers = internshipOfferService.getAllOpenInternshipOffersAsDTO();
        return ResponseEntity.ok(offers);
    }

    /**
     * ✅ Return offers created by a company branch user (for My Internship Offers page)
     */
    @GetMapping("/company/{userName}")
    public ResponseEntity<List<InternshipOfferDTO>> getCompanyInternshipOffers(@PathVariable String userName) {
        return ResponseEntity.ok(internshipOfferService.getCompanyInternshipOffers(userName));
    }


    @PutMapping("/update/{offerId}")
    public ResponseEntity<Map<String, String>> updateInternshipOffer(
            @PathVariable Integer offerId,
            @RequestParam String position,
            @RequestParam String department,
            @RequestParam String details,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String status) {

        internshipOfferService.updateInternshipOffer(offerId, position, department, details, startDate, endDate, status);
        return ResponseEntity.ok(Map.of("message", "Internship offer updated successfully."));
    }

    @DeleteMapping("/delete/{offerId}")
    public ResponseEntity<Map<String, String>> deleteInternshipOffer(@PathVariable Integer offerId) {
        internshipOfferService.deleteInternshipOffer(offerId);
        return ResponseEntity.ok(Map.of("message", "Internship offer deleted successfully."));
    }
}