package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;

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
            CompanyRepository companyRepository ,
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
    }

    /**
     * Retrieves all approved internships as DTOs using a direct JPQL projection for better performance.
     *
     * @return A list of approved trainee information form DTOs
     */
    @GetMapping
    public ResponseEntity<List<ApprovedTraineeInformationFormDTO>> getAllInternships() {
        List<ApprovedTraineeInformationFormDTO> internshipDTOs = approvedTraineeInformationFormRepository.findAllInternshipDTOs();
        return ResponseEntity.ok(internshipDTOs);
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