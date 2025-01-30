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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/traineeFormStudent")
public class TraineeStudentFormStudentController {

    @Autowired
    private InitialTraineeInformationFormService initialTraineeInformationFormService;

    @Autowired
    private ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> getAllTraineeForms(@RequestBody String username) {
        String userName = username;

        List<InitialTraineeInformationForm> initialTraineeInformationForms =
                initialTraineeInformationFormService.getAllInitialTraineeInformationFormofStudent(userName);

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
                approvedTraineeInformationFormService.getAllApprovedTraineeInformationFormofStudent(userName);

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
                        form.getEvaluateForms().stream()
                                .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                                .toList(),
                        form.getReports().stream()
                                .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback()))
                                .toList()
                ))
                .toList();

        // Combine both lists into a single response
        List<Object> response = List.of(initialTraineeInformationFormDTOs, approvedTraineeInformationFormDTOs);

        return ResponseEntity.ok(response);
    }
}