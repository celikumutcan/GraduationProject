package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipOfferService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferCreateDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferListDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internship-offers")
public class InternshipOfferController {

    private final InternshipOfferService internshipOfferService;

    public InternshipOfferController(InternshipOfferService internshipOfferService) {
        this.internshipOfferService = internshipOfferService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createInternshipOffer(
            @RequestParam("position") String position,
            @RequestParam("department") String department,
            @RequestParam("details") String details,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("companyUserName") String companyUserName,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        internshipOfferService.createInternshipOfferWithImage(
                position, department, details, startDate, endDate,
                description, companyUserName, imageFile
        );

        return ResponseEntity.ok(Map.of("message", "Internship offer created successfully."));
    }


    @GetMapping("/open")
    public ResponseEntity<List<InternshipOfferListDTO>> getAllOpenInternshipOffers() {
        List<InternshipOfferListDTO> offers = internshipOfferService.getAllOpenInternshipOffersAsDTO();
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{offerId}/image")
    public ResponseEntity<byte[]> getOfferImage(@PathVariable Integer offerId) {
        InternshipOffer offer = internshipOfferService.getOfferById(offerId);
        byte[] image = offer.getImageData();

        if (image == null || offer.getImageType() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", offer.getImageType())
                .body(image);
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