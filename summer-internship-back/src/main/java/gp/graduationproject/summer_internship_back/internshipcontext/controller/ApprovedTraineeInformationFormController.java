package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to handle browsing internships.
 */
@RestController
@RequestMapping("/api/internships")
public class ApprovedTraineeInformationFormController {

    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    @Autowired
    public ApprovedTraineeInformationFormController(ApprovedTraineeInformationFormService approvedTraineeInformationFormService) {
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
    }

    /**
     * Retrieves all approved internships with optional filtering.
     *
     * @param country  Optional filter by country
     * @param city     Optional filter by city
     * @param district Optional filter by district
     * @param position Optional filter by position
     * @return List of internships
     */
    @GetMapping
    public ResponseEntity<List<ApprovedTraineeInformationFormDTO>> getAllInternships(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String position
    ) {
        List<ApprovedTraineeInformationForm> internships = approvedTraineeInformationFormService.getApprovedTraineeInformationForms();

        // Apply filters
        if (country != null) {
            internships = internships.stream()
                    .filter(internship -> internship.getCompanyBranch().getCountry().equalsIgnoreCase(country))
                    .collect(Collectors.toList());
        }
        if (city != null) {
            internships = internships.stream()
                    .filter(internship -> internship.getCompanyBranch().getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        }
        if (district != null) {
            internships = internships.stream()
                    .filter(internship -> internship.getCompanyBranch().getDistrict().equalsIgnoreCase(district))
                    .collect(Collectors.toList());
        }
        if (position != null) {
            internships = internships.stream()
                    .filter(internship -> internship.getPosition().equalsIgnoreCase(position))
                    .collect(Collectors.toList());
        }

        // Convert to DTO
        List<ApprovedTraineeInformationFormDTO> internshipDTOs = internships.stream()
                .map(internship -> new ApprovedTraineeInformationFormDTO(
                        internship.getId(),
                        internship.getFillUserName().getUsers().getFirstName(),
                        internship.getFillUserName().getUsers().getLastName(),
                        internship.getFillUserName().getUserName(),
                        internship.getDatetime(),
                        internship.getPosition(),
                        internship.getType(),
                        internship.getCode(),
                        internship.getSemester(),
                        internship.getSupervisorName(),
                        internship.getSupervisorSurname(),
                        internship.getHealthInsurance(),
                        internship.getInsuranceApproval(),
                        internship.getInsuranceApprovalDate(),
                        internship.getStatus(),
                        internship.getCompanyBranch().getCompanyUserName().getUserName(),
                        internship.getCompanyBranch().getBranchName(),
                        internship.getCompanyBranch().getAddress(),
                        internship.getCompanyBranch().getPhone(),
                        internship.getCompanyBranch().getBranchEmail(),
                        internship.getCompanyBranch().getCountry(),
                        internship.getCompanyBranch().getCity(),
                        internship.getCompanyBranch().getDistrict(),
                        internship.getEvaluateForms().stream()
                                .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                                .collect(Collectors.toList()),
                        internship.getReports().stream()
                                .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(internshipDTOs);
    }
}