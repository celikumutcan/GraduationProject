package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/traineeFormInstructor")
public class TraineeStudentFormInstructorController {

    @Autowired
    private ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> getAllTraineeForms(@RequestBody String username) {
        String userName = username;

        List<ApprovedTraineeInformationForm> approvedTraineeInformationForms =
                approvedTraineeInformationFormService.getAllApprovedTraineeInformationFormofInstructor(userName);

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

        List<Object> response = Collections.singletonList(approvedTraineeInformationFormDTOs);
        return ResponseEntity.ok(response);
    }
}