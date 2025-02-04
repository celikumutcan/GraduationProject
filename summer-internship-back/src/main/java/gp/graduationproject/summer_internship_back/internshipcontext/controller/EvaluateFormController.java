package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EvaluateFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public EvaluateFormController(EvaluateFormService evaluateFormService)
    {
        this.evaluateFormService = evaluateFormService;
    }

    /**
     * Creates a new evaluation form for an approved trainee information form.
     *
     * @param traineeFormId The ID of the trainee form.
     * @param workingDay    The number of days the student attended.
     * @param performance   The performance rating of the student.
     * @param feedback      Additional feedback about the student.
     * @return Response indicating success or failure.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createEvaluationForm(
            @RequestParam Integer traineeFormId,
            @RequestParam Integer workingDay,
            @RequestParam String performance,
            @RequestParam String feedback)
    {
        evaluateFormService.createEvaluationForm(traineeFormId, workingDay, performance, feedback);
        return ResponseEntity.ok("Evaluation form created successfully.");
    }

    /**
     * Retrieves all evaluation forms for a specific trainee's internship.
     *
     * @param traineeFormId The ID of the trainee form.
     * @return List of evaluation forms.
     */
    @GetMapping("/trainee/{traineeFormId}")
    public ResponseEntity<List<EvaluateForm>> getEvaluationsByTraineeForm(@PathVariable Integer traineeFormId)
    {
        List<EvaluateForm> evaluations = evaluateFormService.getEvaluationsByTraineeForm(traineeFormId);
        return ResponseEntity.ok(evaluations);
    }
}