package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Controller for handling instructor-related trainee forms.
 */
@RestController
@RequestMapping("/api/traineeFormInstructor")
public class TraineeStudentFormInstructorController {

    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    /**
     * Constructor-based dependency injection.
     *
     * @param approvedTraineeInformationFormService Service for handling approved trainee forms.
     */
    public TraineeStudentFormInstructorController(ApprovedTraineeInformationFormService approvedTraineeInformationFormService) {
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
    }

    /**
     * Retrieves all approved trainee forms for a given instructor.
     *
     * @param username The instructor's username.
     * @return List of approved trainee forms.
     */
    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> getAllTraineeForms(@RequestBody String username) {
        List<ApprovedTraineeInformationForm> approvedTraineeInformationForms =
                approvedTraineeInformationFormService.getAllApprovedTraineeInformationFormOfInstructor(username);

        List<ApprovedTraineeInformationFormDTO> approvedTraineeInformationFormDTOs = approvedTraineeInformationForms.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(Collections.singletonList(approvedTraineeInformationFormDTOs));
    }

    /**
     * Converts an ApprovedTraineeInformationForm entity to its DTO representation.
     *
     * @param form The entity to be converted.
     * @return The corresponding DTO.
     */
    private ApprovedTraineeInformationFormDTO convertToDTO(ApprovedTraineeInformationForm form) {
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
                form.getEvaluatingFacultyMember() != null ? form.getEvaluatingFacultyMember() : "Unknown",
                form.getInternshipStartDate(),
                form.getInternshipEndDate(),
                form.getEvaluateForms().stream()
                        .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                        .toList(),
                form.getReports().stream()
                        .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback()))
                        .toList()
        );
    }
}
