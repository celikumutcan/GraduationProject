package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.EvaluateFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class EvaluateFormService {

    private final EvaluateFormRepository evaluateFormRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;

    public EvaluateFormService(EvaluateFormRepository evaluateFormRepository,
                               ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository)
    {
        this.evaluateFormRepository = evaluateFormRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
    }

    /**
     * Saves a new evaluation form for an approved trainee information form.
     *
     * @param traineeFormId The ID of the approved trainee form.
     * @param workingDay    The number of days the student attended.
     * @param performance   The performance rating of the student.
     * @param feedback      Additional feedback about the student.
     */
    @Transactional
    public void createEvaluationForm(Integer traineeFormId, Integer workingDay, String performance, String feedback)
    {
        ApprovedTraineeInformationForm traineeForm = approvedTraineeInformationFormRepository.findById(traineeFormId)
                .orElseThrow(() -> new RuntimeException("Trainee form not found."));

        EvaluateForm evaluation = new EvaluateForm();
        evaluation.setTraineeInformationForm(traineeForm);
        evaluation.setWorkingDay(workingDay);
        evaluation.setPerformance(performance);
        evaluation.setFeedback(feedback);

        evaluateFormRepository.save(evaluation);
    }

    /**
     * Retrieves all evaluations for a specific trainee's internship.
     *
     * @param traineeFormId The ID of the approved trainee form.
     * @return List of evaluations.
     */
    @Transactional
    public List<EvaluateForm> getEvaluationsByTraineeForm(Integer traineeFormId)
    {
        ApprovedTraineeInformationForm traineeForm = approvedTraineeInformationFormRepository.findById(traineeFormId)
                .orElseThrow(() -> new RuntimeException("Trainee form not found."));
        return evaluateFormRepository.findByTraineeInformationForm(traineeForm);
    }
}