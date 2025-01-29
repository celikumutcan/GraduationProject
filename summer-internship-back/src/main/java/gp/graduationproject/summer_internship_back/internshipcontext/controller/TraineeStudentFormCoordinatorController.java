package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InitialTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/traineeFormCoordinator")
public class TraineeStudentFormCoordinatorController {

    @Autowired
    private InitialTraineeInformationFormService initialTraineeInformationFormService;

    @Autowired
    private ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    @GetMapping
    public ResponseEntity<List<Object>> getAllTraineeStudentForms() {
        List<InitialTraineeInformationForm> initialTraineeInformationForms =
                initialTraineeInformationFormService.getInitialTraineeInformationForms();

        List<InitialTraineeInformationFormDTO> initialTraineeInformationFormDTOs = initialTraineeInformationForms.stream()
                .map(form -> new InitialTraineeInformationFormDTO(
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
                        form.getInternshipEndDate()
                ))
                .toList();

        List<ApprovedTraineeInformationForm> approvedTraineeInformationForms =
                approvedTraineeInformationFormService.getApprovedTraineeInformationForms();

        List<ApprovedTraineeInformationFormDTO> approvedTraineeInformationFormDTOs = approvedTraineeInformationForms.stream()
                .map(form -> new ApprovedTraineeInformationFormDTO(
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
                        form.getBranch().getCompanyUserName().getUserName(),
                        form.getBranch().getBranchName(),
                        form.getBranch().getAddress(),
                        form.getBranch().getPhone(),
                        form.getBranch().getBranchEmail(),
                        form.getBranch().getCountry(),  // ✅ Eksik olan country eklendi
                        form.getBranch().getCity(),     // ✅ Eksik olan city eklendi
                        form.getBranch().getDistrict(), // ✅ Eksik olan district eklendi
                        form.getEvaluateForms().stream()
                                .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                                .toList(),
                        form.getReports().stream()
                                .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback()))
                                .toList()
                ))
                .toList();

        List<Object> response = List.of(initialTraineeInformationFormDTOs, approvedTraineeInformationFormDTOs);

        return ResponseEntity.ok(response);
    }
}