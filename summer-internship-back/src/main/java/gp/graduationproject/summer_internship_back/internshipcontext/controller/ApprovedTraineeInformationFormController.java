package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/**
 * Controller to handle browsing internships.
 */
@RestController
@RequestMapping("/api/internships")
public class ApprovedTraineeInformationFormController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    private  ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    @Autowired
    private CompanyBranchService companyBranchService;



    @Autowired
    public ApprovedTraineeInformationFormController(ApprovedTraineeInformationFormService approvedTraineeInformationFormService) {
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
    }

    @Autowired
    private ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;

    public ApprovedTraineeInformationFormController(
            StudentRepository studentRepository,
            CompanyRepository companyRepository ,// Buraya ekle
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
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
                    .filter(internship -> Optional.ofNullable(internship.getCompanyBranch())
                            .map(branch -> country.equalsIgnoreCase(branch.getCountry()))
                            .orElse(false))
                    .toList();
        }
        if (city != null) {
            internships = internships.stream()
                    .filter(internship -> Optional.ofNullable(internship.getCompanyBranch())
                            .map(branch -> city.equalsIgnoreCase(branch.getCity()))
                            .orElse(false))
                    .toList();
        }
        if (district != null) {
            internships = internships.stream()
                    .filter(internship -> Optional.ofNullable(internship.getCompanyBranch())
                            .map(branch -> district.equalsIgnoreCase(branch.getDistrict()))
                            .orElse(false))
                    .toList();
        }
        if (position != null) {
            internships = internships.stream()
                    .filter(internship -> position.equalsIgnoreCase(internship.getPosition()))
                    .toList();
        }

        // Convert to DTO
        List<ApprovedTraineeInformationFormDTO> internshipDTOs = internships.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(internshipDTOs);
    }

    /**
     * Converts an ApprovedTraineeInformationForm to ApprovedTraineeInformationFormDTO.
     *
     * @param internship Internship entity to be converted
     * @return Converted DTO object
     */
    private ApprovedTraineeInformationFormDTO convertToDTO(ApprovedTraineeInformationForm internship) {
        return new ApprovedTraineeInformationFormDTO(
                internship.getId(),
                Optional.ofNullable(internship.getFillUserName())
                        .map(user -> user.getUsers().getFirstName())
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getFillUserName())
                        .map(user -> user.getUsers().getLastName())
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getFillUserName())
                        .map(user -> user.getUserName())
                        .orElse("Unknown"),
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
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(branch -> branch.getCompanyUserName().getUserName())
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getBranchName)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getAddress)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getPhone)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getBranchEmail)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getCountry)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getCity)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCompanyBranch())
                        .map(CompanyBranch::getDistrict)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getCoordinatorUserName())
                        .map(AcademicStaff::getUserName)
                        .orElse("Unknown"),
                Optional.ofNullable(internship.getEvaluatingFacultyMember())
                        .orElse("Unknown"),
                internship.getInternshipStartDate(),
                internship.getInternshipEndDate(),
                internship.getEvaluateForms().stream()
                        .map(EvaluateFormDTO::new)
                        .toList(),
                internship.getReports().stream()
                        .map(ReportDTO::new)
                        .toList()
        );
    }

    /**
     * Retrieves details of an approved trainee form by ID.
     *
     * @param id The ID of the approved trainee form.
     * @return The approved trainee form details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApprovedTraineeInformationForm> getApprovedTraineeFormById(@PathVariable Integer id) {
        Optional<ApprovedTraineeInformationForm> form = approvedTraineeInformationFormService.getApprovedTraineeInformationFormById(id);
        return form.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PostMapping("/add")
    public ResponseEntity<String> addTraineeForm(@RequestBody ApprovedTraineeInformationFormDTO formDTO) {
        try {
            ApprovedTraineeInformationForm form = new ApprovedTraineeInformationForm();

            // Öğrenci bilgisini çek
            form.setFillUserName(studentRepository.findByUserName(formDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("Student not found")));

            form.setDatetime(formDTO.getDatetime());
            form.setPosition(formDTO.getPosition());
            form.setType(formDTO.getType());
            form.setCode(formDTO.getCode());
            form.setSemester(formDTO.getSemester());
            form.setSupervisorName(formDTO.getSupervisorName());
            form.setSupervisorSurname(formDTO.getSupervisorSurname());
            form.setHealthInsurance(formDTO.getHealthInsurance());
            form.setStatus("Pending");
            form.setInternshipStartDate(formDTO.getInternshipStartDate());
            form.setInternshipEndDate(formDTO.getInternshipEndDate());

            // **Şirket bilgilerini eklemeden önce veritabanına kaydet**
            CompanyBranch companyBranch = new CompanyBranch();
            companyBranch.setBranchName(formDTO.getBranchName());
            companyBranch.setAddress(formDTO.getCompanyAddress());
            companyBranch.setPhone(formDTO.getCompanyPhone());
            companyBranch.setBranchEmail(formDTO.getCompanyEmail());
            companyBranch.setCountry(formDTO.getCountry());
            companyBranch.setCity(formDTO.getCity());
            companyBranch.setDistrict(formDTO.getDistrict());

            // **CompanyBranch nesnesini kaydet**
            companyBranch = companyBranchService.saveCompanyBranch(companyBranch);

            // **Şirket bilgilerini forma ekle**
            form.setCompanyBranch(companyBranch);

            // Değerlendirme yapan hocayı ayarla
            form.setEvaluatingFacultyMember(formDTO.getEvaluateUserName());

            // **Son olarak formu kaydet**
            approvedTraineeInformationFormRepository.save(form);
            return ResponseEntity.ok("Trainee form added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }



}