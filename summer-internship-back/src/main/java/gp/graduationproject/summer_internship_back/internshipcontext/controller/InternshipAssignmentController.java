package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.InternshipAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internship-assignments") // 📌 API'nin base path'i
public class InternshipAssignmentController {

    private final InternshipAssignmentService internshipAssignmentService;

    public InternshipAssignmentController(InternshipAssignmentService internshipAssignmentService) {
        this.internshipAssignmentService = internshipAssignmentService;
    }

    @PostMapping("/assign") // 📌 Stajları eğitmenlere dağıtan endpoint
    public ResponseEntity<String> assignInternships() {
        String result = internshipAssignmentService.assignInternshipsToInstructors();
        return ResponseEntity.ok(result);
    }
}