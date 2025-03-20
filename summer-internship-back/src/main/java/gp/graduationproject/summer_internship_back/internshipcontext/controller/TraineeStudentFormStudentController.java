package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
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
            ApprovedTraineeInformationFormService approvedTraineeInformationFormService) {
        this.initialTraineeInformationFormService = initialTraineeInformationFormService;
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> getAllTraineeForms(@RequestBody String username) {
        List<InitialTraineeInformationFormDTO> initialFormDTOs = initialTraineeInformationFormService
                .getAllInitialTraineeInformationFormOfStudent(username)
                .stream().map(this::convertToInitialDTO).toList();

        List<ApprovedTraineeInformationFormDTO> approvedFormDTOs = approvedTraineeInformationFormService
                .getAllApprovedTraineeInformationFormOfStudent(username)
                .stream().map(this::convertToApprovedDTO).toList();

        return ResponseEntity.ok(List.of(initialFormDTOs, approvedFormDTOs));
    }

    @DeleteMapping("/initial/{id}")
    @Transactional
    public ResponseEntity<Void> deleteInitialTraineeForm(@PathVariable Integer id, @RequestParam String username) {
        boolean deleted = initialTraineeInformationFormService.deleteInitialTraineeInformationForm(id, username);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/approved/{id}")
    @Transactional
    public ResponseEntity<Void> deleteApprovedTraineeForm(@PathVariable Integer id, @RequestParam String username) {
        boolean deleted = approvedTraineeInformationFormService.deleteApprovedTraineeInformationForm(id, username);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/initial/{id}")
    @Transactional
    public ResponseEntity<InitialTraineeInformationFormDTO> updateInitialTraineeForm(
            @PathVariable Integer id,
            @RequestParam String username,
            @RequestBody InitialTraineeInformationFormDTO updatedFormDTO) {

        InitialTraineeInformationForm updatedForm = convertToInitialEntity(updatedFormDTO);
        InitialTraineeInformationForm result = initialTraineeInformationFormService.updateInitialTraineeInformationForm(id, username, updatedForm);
        return ResponseEntity.ok(convertToInitialDTO(result));
    }

    @PutMapping("/approved/{id}")
    @Transactional
    public ResponseEntity<ApprovedTraineeInformationFormDTO> updateApprovedTraineeForm(
            @PathVariable Integer id,
            @RequestParam String username,
            @RequestBody ApprovedTraineeInformationFormDTO updatedFormDTO) {

        ApprovedTraineeInformationForm updatedForm = convertToApprovedEntity(updatedFormDTO);
        ApprovedTraineeInformationForm result = approvedTraineeInformationFormService.updateApprovedTraineeInformationForm(id, username, updatedForm);
        return ResponseEntity.ok(convertToApprovedDTO(result));
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
                getUserNameOrUnknown(form.getCoordinatorUserName()),
                form.getEvaluatingFacultyMember() != null ? form.getEvaluatingFacultyMember() : "Unknown"
        );
    }

    private ApprovedTraineeInformationFormDTO convertToApprovedDTO(ApprovedTraineeInformationForm form) {
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
                getUserNameOrUnknown(form.getCoordinatorUserName()),
                form.getEvaluatingFacultyMember(),
                form.getInternshipStartDate(),
                form.getInternshipEndDate(),
                form.getEvaluateForms().stream()
                        .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                        .toList()
        );
    }


    private InitialTraineeInformationForm convertToInitialEntity(InitialTraineeInformationFormDTO dto) {
        InitialTraineeInformationForm form = new InitialTraineeInformationForm();
        form.setId(dto.getId());
        form.setDatetime(dto.getDatetime());
        form.setPosition(dto.getPosition());
        form.setType(dto.getType());
        form.setCode(dto.getCode());
        form.setSemester(dto.getSemester());
        form.setSupervisorName(dto.getSupervisorName());
        form.setSupervisorSurname(dto.getSupervisorSurname());
        form.setHealthInsurance(dto.getHealthInsurance());
        form.setStatus(dto.getStatus());
        form.setBranchName(dto.getBranchName());
        form.setCompanyUserName(dto.getCompanyUserName());
        form.setCountry(dto.getCountry());
        form.setCity(dto.getCity());
        form.setDistrict(dto.getDistrict());
        form.setInternshipStartDate(dto.getStartDate());
        form.setInternshipEndDate(dto.getEndDate());
        return form;
    }

    private ApprovedTraineeInformationForm convertToApprovedEntity(ApprovedTraineeInformationFormDTO dto) {
        ApprovedTraineeInformationForm form = new ApprovedTraineeInformationForm();
        form.setId(dto.getId());
        form.setDatetime(dto.getDatetime());
        form.setPosition(dto.getPosition());
        form.setType(dto.getType());
        form.setCode(dto.getCode());
        form.setSemester(dto.getSemester());
        form.setSupervisorName(dto.getSupervisorName());
        form.setSupervisorSurname(dto.getSupervisorSurname());
        form.setHealthInsurance(dto.getHealthInsurance());
        form.setStatus(dto.getStatus());
        form.setInternshipStartDate(dto.getInternshipStartDate());
        form.setInternshipEndDate(dto.getInternshipEndDate());
        return form;
    }

    private String getUserNameOrUnknown(AcademicStaff user)
    {
        return (user != null) ? user.getUserName() : "Unknown";
    }
}