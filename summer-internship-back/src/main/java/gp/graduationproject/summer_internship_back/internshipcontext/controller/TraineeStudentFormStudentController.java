package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InitialTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traineeFormStudent")
public class TraineeStudentFormStudentController {

    private final InitialTraineeInformationFormService initialTraineeInformationFormService;
    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    public TraineeStudentFormStudentController(
            InitialTraineeInformationFormService initialTraineeInformationFormService,
            ApprovedTraineeInformationFormService approvedTraineeInformationFormService)
    {
        this.initialTraineeInformationFormService = initialTraineeInformationFormService;
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> getAllTraineeForms(@RequestBody String username)
    {
        // Fetch initial forms
        List<InitialTraineeInformationFormDTO> initialFormDTOs = initialTraineeInformationFormService
                .getAllInitialTraineeInformationFormOfStudent(username)
                .stream()
                .map(this::convertToInitialDTO)
                .toList();

        // Fetch approved forms
        List<ApprovedTraineeInformationFormDTO> approvedFormDTOs = approvedTraineeInformationFormService
                .getAllApprovedTraineeInformationFormOfStudent(username)
                .stream()
                .map(this::convertToApprovedDTO)
                .toList();

        // Return both initial and approved forms as response
        return ResponseEntity.ok(List.of(initialFormDTOs, approvedFormDTOs));
    }

    private InitialTraineeInformationFormDTO convertToInitialDTO(InitialTraineeInformationForm form) {
        return new InitialTraineeInformationFormDTO(
                form.getId(),
                form.getFillUserName().getUsers().getFirstName(),
                form.getFillUserName().getUsers().getLastName(),
                form.getFillUserName().getUserName(),
                form.getDatetime(),
                form.getPosition(),
                form.getType(),
                form.getCode(),
                form.getSemester(),
                form.getSupervisorName(),
                form.getSupervisorSurname(),
                form.getHealthInsurance(),
                form.getStatus(),
                form.getCompanyUserName(),
                form.getBranchName(),
                form.getCompanyBranchAddress(),
                form.getCompanyBranchPhone(),
                form.getCompanyBranchEmail(),
                form.getCountry(),
                form.getCity(),
                form.getDistrict(),
                form.getInternshipStartDate(),
                form.getInternshipEndDate(),
                form.getCoordinatorUserName() != null ? form.getCoordinatorUserName().getUserName() : "Unknown",
                form.getEvaluatingFacultyMember() != null ? form.getEvaluatingFacultyMember() : "Unknown"
        );
    }

    private ApprovedTraineeInformationFormDTO convertToApprovedDTO(ApprovedTraineeInformationForm form)
    {
        return new ApprovedTraineeInformationFormDTO(
                form.getId(),
                form.getFillUserName().getUsers().getFirstName(),
                form.getFillUserName().getUsers().getLastName(),
                form.getFillUserName().getUserName(),
                form.getDatetime(),
                form.getPosition(),
                form.getType(),
                form.getCode(),
                form.getSemester(),
                form.getSupervisorName(),
                form.getSupervisorSurname(),
                form.getHealthInsurance(),
                form.getInsuranceApproval(),
                form.getInsuranceApprovalDate(),
                form.getStatus(),
                form.getCompanyBranch().getCompanyUserName().getUserName(),
                form.getCompanyBranch().getBranchName(),
                form.getCompanyBranch().getAddress(),
                form.getCompanyBranch().getPhone(),
                form.getCompanyBranch().getBranchEmail(),
                form.getCompanyBranch().getCountry(),
                form.getCompanyBranch().getCity(),
                form.getCompanyBranch().getDistrict(),
                form.getCoordinatorUserName() != null ? form.getCoordinatorUserName().getUserName() : "Unknown",
                form.getEvaluatingFacultyMember(),
                form.getInternshipStartDate(),
                form.getInternshipEndDate(),
                form.getEvaluateForms().stream()
                        .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                        .toList(),
                form.getReports().stream()
                        .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback(), r.getStatus()))
                        .toList()
        );
    }

    /**
     * Handles the deletion of an initial trainee information form.
     *
     * Only the user who created the form is allowed to delete it. If the deletion is successful,
     * a 204 No Content response is returned. If the user is not authorized, a 403 Forbidden response is returned.
     *
     * @param id       The ID of the trainee form to be deleted.
     * @param username The username of the student attempting to delete the form.
     * @return {@code ResponseEntity<Void>} with a status of 204 (No Content) if successful,
     *         or 403 (Forbidden) if the user is not authorized to delete the form.
     */
    @DeleteMapping("/initial/{id}")
    @Transactional
    public ResponseEntity<Void> deleteInitialTraineeForm(@PathVariable Integer id, @RequestParam String username) {
        boolean deleted = initialTraineeInformationFormService.deleteInitialTraineeInformationForm(id, username);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Deletes an approved trainee form if the user owns it.
     *
     * @param id       The ID of the form to be deleted.
     * @param username The username of the user requesting the deletion.
     * @return 204 No Content if successful, 403 Forbidden if unauthorized.
     */
    @DeleteMapping("/approved/{id}")
    @Transactional
    public ResponseEntity<Void> deleteApprovedTraineeForm(@PathVariable Integer id, @RequestParam String username) {
        boolean deleted = approvedTraineeInformationFormService.deleteApprovedTraineeInformationForm(id, username);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}