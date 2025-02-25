package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/traineeFormCompany")
public class TraineeStudentFormCompanyController {

    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;
    private final CompanyBranchRepository companyBranchRepository;
    private final UserRepository userRepository;

    public TraineeStudentFormCompanyController(
            ApprovedTraineeInformationFormService approvedTraineeInformationFormService,
            CompanyBranchRepository companyBranchRepository,
            UserRepository userRepository)
    {
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
        this.companyBranchRepository = companyBranchRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> getAllTraineeForms(@RequestBody String username)
    {
        String userName = username;
        User user = userRepository.findByUserName(userName);
        CompanyBranch branch = companyBranchRepository.findByBranchUserName(user)
                .orElseThrow(() -> new RuntimeException("Company branch not found for user: " + user.getUserName()));
        List<ApprovedTraineeInformationForm> approvedForms =
                approvedTraineeInformationFormService.getAllApprovedTraineeInformationFormOfCompany(branch.getId());

        List<ApprovedTraineeInformationFormDTO> approvedDTOs = approvedForms.stream()
                .map(this::convertToApprovedDTO)
                .toList();

        return ResponseEntity.ok(Collections.singletonList(approvedDTOs));
    }

    @PostMapping("/approveInternship")
    @Transactional
    public ResponseEntity<String> approveInternship(@RequestParam Integer internshipId)
    {
        approvedTraineeInformationFormService.approveInternship(internshipId);
        return ResponseEntity.ok("Internship approved successfully.");
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
                form.getCoordinatorUserName().getUserName(),
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
}