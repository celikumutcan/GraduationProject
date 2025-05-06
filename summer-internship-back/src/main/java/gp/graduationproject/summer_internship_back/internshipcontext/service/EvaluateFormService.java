package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.EvaluateFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service class for managing evaluation forms.
 */
@Service
public class EvaluateFormService {

    private final EvaluateFormRepository evaluateFormRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final EmailService emailService;

    public EvaluateFormService(EvaluateFormRepository evaluateFormRepository,
                               ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
                               EmailService emailService) {
        this.evaluateFormRepository = evaluateFormRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.emailService = emailService;
    }

    /**
     * Saves a new evaluation form and notifies the student by email.
     *
     * @param traineeFormId The trainee form ID
     * @param attendance Attendance evaluation
     * @param diligenceAndEnthusiasm Diligence and enthusiasm evaluation
     * @param contributionToWorkEnvironment Contribution to work environment evaluation
     * @param overallPerformance Overall performance evaluation
     * @param comments Additional comments
     */
    @Transactional
    public void createEvaluationForm(Integer traineeFormId, String attendance, String diligenceAndEnthusiasm,
                                     String contributionToWorkEnvironment, String overallPerformance, String comments) {
        ApprovedTraineeInformationForm traineeForm = approvedTraineeInformationFormRepository.findById(traineeFormId)
                .orElseThrow(() -> new RuntimeException("Trainee form not found."));

        EvaluateForm evaluation = new EvaluateForm();
        evaluation.setTraineeInformationForm(traineeForm);
        evaluation.setAttendance(attendance);
        evaluation.setDiligenceAndEnthusiasm(diligenceAndEnthusiasm);
        evaluation.setContributionToWorkEnvironment(contributionToWorkEnvironment);
        evaluation.setOverallPerformance(overallPerformance);
        evaluation.setComments(comments);

        evaluateFormRepository.save(evaluation);

        // Notify student by email
        String studentEmail = traineeForm.getFillUserName().getUsers().getEmail();
        if (studentEmail != null && !studentEmail.isBlank()) {
            String subject = "Your Internship Evaluation Has Been Submitted";
            String body = """
                Dear Student,

                We would like to inform you that your internship evaluation has been successfully completed by the company.

                Kind regards,
                Internship Management Team
                """;
            emailService.sendEmail(studentEmail, subject, body);
        }
    }

    /**
     * Retrieves all evaluations for a specific trainee's internship.
     *
     * @param traineeFormId The ID of the approved trainee form.
     * @return List of evaluations.
     */
    @Transactional
    public List<EvaluateForm> getEvaluationsByTraineeForm(Integer traineeFormId) {
        ApprovedTraineeInformationForm traineeForm = approvedTraineeInformationFormRepository.findById(traineeFormId)
                .orElseThrow(() -> new RuntimeException("Trainee form not found."));
        return evaluateFormRepository.findByTraineeInformationForm(traineeForm);
    }

    public EmailService getEmailService() {
        return emailService;
    }
}