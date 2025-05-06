package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EvaluateFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to handle evaluation forms for approved trainee internships.
 */
@RestController
@RequestMapping("/api/evaluation")
public class EvaluateFormController {

    private final EvaluateFormService evaluateFormService;

    /**
     * Constructor-based dependency injection.
     */
    public EvaluateFormController(EvaluateFormService evaluateFormService) {
        this.evaluateFormService = evaluateFormService;
    }

    /**
     * Creates a new evaluation form for an approved trainee information form.
     *
     * @param traineeFormId The ID of the trainee form.
     * @param attendance Attendance evaluation
     * @param diligenceAndEnthusiasm Diligence and enthusiasm evaluation
     * @param contributionToWorkEnvironment Contribution to work environment evaluation
     * @param overallPerformance Overall performance evaluation
     * @param comments Additional comments
     * @return Response indicating success or failure.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createEvaluationForm(
            @RequestParam Integer traineeFormId,
            @RequestParam String attendance,
            @RequestParam String diligenceAndEnthusiasm,
            @RequestParam String contributionToWorkEnvironment,
            @RequestParam String overallPerformance,
            @RequestParam String comments)
    {
        evaluateFormService.createEvaluationForm(
                traineeFormId,
                attendance,
                diligenceAndEnthusiasm,
                contributionToWorkEnvironment,
                overallPerformance,
                comments
        );
        return ResponseEntity.ok("Evaluation form created successfully.");
    }

    /**
     * Retrieves all evaluation forms for a specific trainee's internship.
     *
     * @param traineeFormId The ID of the trainee form.
     * @return List of evaluation form DTOs.
     */
    @GetMapping("/trainee/{traineeFormId}")
    public ResponseEntity<List<EvaluateFormDTO>> getEvaluationsByTraineeForm(@PathVariable Integer traineeFormId) {
        List<EvaluateForm> evaluations = evaluateFormService.getEvaluationsByTraineeForm(traineeFormId);
        List<EvaluateFormDTO> evaluationDTOs = evaluations.stream()
                .map(EvaluateFormDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(evaluationDTOs);
    }
}