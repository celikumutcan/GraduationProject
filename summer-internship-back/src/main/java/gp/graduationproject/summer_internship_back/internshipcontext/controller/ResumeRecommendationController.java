package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeRecommendationService;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class ResumeRecommendationController {

    private final ResumeRecommendationService resumeRecommendationService;

    @Autowired
    public ResumeRecommendationController(ResumeRecommendationService resumeRecommendationService) {
        this.resumeRecommendationService = resumeRecommendationService;
    }

    @GetMapping("/recommended/{username}")
    public ResponseEntity<List<InternshipOffer>> getRecommendedInternships(@PathVariable String username) {
        List<InternshipOffer> recommendedInternships = resumeRecommendationService.recommendInternships(username);

        System.out.println("📌 Postman'a Gönderilen Stajlar: " + recommendedInternships);

        return ResponseEntity.ok(recommendedInternships);
    }
}