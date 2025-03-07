package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.time.temporal.ChronoUnit;


@RestController
@RequestMapping("/api/initialTraineeInformationForm")
public class InitialTraineeInformationFormController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final AcademicStaffRepository academicStaffRepository;
    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;

    public InitialTraineeInformationFormController(
            StudentRepository studentRepository,
            UserRepository userRepository,
            AcademicStaffRepository academicStaffRepository,
            InitialTraineeInformationFormRepository initialTraineeInformationFormRepository,
            CompanyBranchRepository companyBranchRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.academicStaffRepository = academicStaffRepository;
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
        this.companyBranchRepository = companyBranchRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> addNewTraineeForm(@RequestBody Map<String, String> payload) {

        String evaluateUserName = payload.get("evaluate_user_name");

        if (evaluateUserName == null || evaluateUserName.isEmpty()) {
            evaluateUserName = "defaultEvaluator"; // Varsayılan bir değer ver
        }
        try {
            String type = payload.get("type");
            String code = payload.get("code");
            String semester = payload.get("semester");
            Boolean health_insurance = Boolean.parseBoolean(payload.get("health_insurance"));
            String company_user_name = payload.get("company_user_name");
            String branch_name = payload.get("branch_name");
            String company_branch_address = payload.getOrDefault("company_branch_address", "");
            String company_branch_phone = payload.getOrDefault("company_branch_phone", "");
            String company_branch_email = payload.getOrDefault("company_branch_email", "");
            String company_branch_country = payload.getOrDefault("company_branch_country", "");
            String company_branch_city = payload.getOrDefault("company_branch_city", "");
            String company_branch_district = payload.getOrDefault("company_branch_district", "");
            LocalDate start_date = LocalDate.parse(payload.get("startDate"));
            LocalDate end_date = LocalDate.parse(payload.get("endDate"));
            long internshipDays = ChronoUnit.DAYS.between(start_date, end_date);
            if (internshipDays < 20) {
                return ResponseEntity.status(400).body(List.of("Error: Internship duration must be at least 20 days."));
            }
            if (company_branch_email.isEmpty() && company_branch_address.isEmpty() && company_branch_phone.isEmpty()) {
                User user1 = userRepository.findByUserName(branch_name);
                CompanyBranch cp = companyBranchRepository.findByBranchUserName(user1)
                        .orElseThrow(() -> new RuntimeException("Company branch not found for user: " + user1.getUserName()));

                company_branch_address = cp.getAddress();
                company_branch_phone = cp.getPhone();
                company_branch_email = cp.getBranchEmail();
                company_branch_country = cp.getCountry();
                company_branch_city = cp.getCity();
                company_branch_district = cp.getDistrict();
            }

            String fill_user_name = payload.get("fill_user_name");
            Student student = studentRepository.findByUserName(fill_user_name)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found with username: " + fill_user_name));

            InitialTraineeInformationForm form = new InitialTraineeInformationForm();
            form.setFillUserName(student);
            form.setType(type);
            form.setCode(code);
            form.setSemester(semester);
            form.setHealthInsurance(health_insurance);
            form.setCompanyUserName(company_user_name);
            form.setCompanyBranchAddress(company_branch_address);
            form.setCompanyBranchPhone(company_branch_phone);
            form.setCompanyBranchEmail(company_branch_email);
            form.setCity(company_branch_city);
            form.setDistrict(company_branch_district);
            form.setCountry(company_branch_country);
            form.setPosition(payload.get("position"));
            form.setDatetime(Instant.now());
            form.setStatus("Coordinator Approval Waiting");
            form.setBranchName(branch_name);
            form.setInternshipStartDate(start_date);
            form.setInternshipEndDate(end_date);
            form.setEvaluatingFacultyMember(evaluateUserName); // ✅ BURAYA EKLE!            form.setSupervisorName(payload.getOrDefault("supervisor_name", ""));
            form.setSupervisorSurname(payload.getOrDefault("supervisor_surname", ""));
            System.out.println("Evaluate User Name: " + evaluateUserName);

            List<User> coordinators = userRepository.findAllByUserType("coordinator");
            User coordinator = coordinators.get(new Random().nextInt(coordinators.size()));
            AcademicStaff academicStaff = academicStaffRepository.findByUserName(coordinator.getUserName())
                    .orElseThrow(() -> new IllegalArgumentException("Coordinator not found with username: " + coordinator.getUserName()));
            form.setCoordinatorUserName(academicStaff);

            initialTraineeInformationFormRepository.save(form);

            return ResponseEntity.status(201).body(List.of("Trainee form created successfully", form));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(List.of("Error creating trainee form: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<List<Object>> editTraineeForm(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        try {
            Optional<InitialTraineeInformationForm> existingForm = initialTraineeInformationFormRepository.findById(id);
            if (existingForm.isEmpty()) {
                return ResponseEntity.status(404).body(List.of("Form not found"));
            }

            InitialTraineeInformationForm form = existingForm.get();

            form.setType(payload.get("type"));
            form.setCode(payload.get("code"));
            form.setSemester(payload.get("semester"));
            form.setHealthInsurance(Boolean.parseBoolean(payload.get("health_insurance")));
            form.setCompanyUserName(payload.get("company_user_name"));
            form.setBranchName(payload.get("branch_name"));
            form.setCompanyBranchAddress(payload.getOrDefault("company_branch_address", form.getCompanyBranchAddress()));
            form.setCompanyBranchPhone(payload.getOrDefault("company_branch_phone", form.getCompanyBranchPhone()));
            form.setCompanyBranchEmail(payload.getOrDefault("company_branch_email", form.getCompanyBranchEmail()));

            form.setCountry(payload.getOrDefault("company_branch_country", form.getCountry()));
            form.setCity(payload.getOrDefault("company_branch_city", form.getCity()));
            form.setDistrict(payload.getOrDefault("company_branch_district", form.getDistrict()));

            form.setInternshipStartDate(LocalDate.parse(payload.get("startDate")));
            form.setInternshipEndDate(LocalDate.parse(payload.get("endDate")));
            form.setPosition(payload.get("position"));
            form.setSupervisorName(payload.get("supervisor_name"));
            form.setSupervisorSurname(payload.get("supervisor_surname"));

            String coordinatorUserName = payload.get("coordinator_user_name");
            AcademicStaff academicStaff = academicStaffRepository.findByUserName(coordinatorUserName)
                    .orElseThrow(() -> new IllegalArgumentException("Coordinator not found with username: " + coordinatorUserName));
            form.setCoordinatorUserName(academicStaff);

            initialTraineeInformationFormRepository.save(form);

            return ResponseEntity.status(200).body(List.of("Trainee form updated successfully", form));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(List.of("Error updating trainee form: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<InitialTraineeInformationForm>> getAllTraineeForms() {
        List<InitialTraineeInformationForm> forms = initialTraineeInformationFormRepository.findAll();
        return ResponseEntity.ok(forms);
    }

    /**
     * Retrieves details of an initial trainee form by ID.
     *
     * @param id The ID of the trainee form.
     * @return The trainee form details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InitialTraineeInformationForm> getInitialTraineeFormById(@PathVariable Integer id) {
        Optional<InitialTraineeInformationForm> form = initialTraineeInformationFormRepository.findById(id);
        return form.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Transactional
    public boolean updateInitialFormStatus(Integer id, String status) {
        Optional<InitialTraineeInformationForm> formOptional = initialTraineeInformationFormRepository.findById(id);

        if (formOptional.isPresent()) {
            InitialTraineeInformationForm form = formOptional.get();
            form.setStatus(status);
            initialTraineeInformationFormRepository.saveAndFlush(form);
            return true;
        }
        return false;
    }
}