package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/initialTraineeInformationForm")
public class InitialTraineeInformationFormController {


    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final AcademicStaffRepository academicStaffRepository;
    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;

    public InitialTraineeInformationFormController(StudentRepository studentRepository, UserRepository userRepository, AcademicStaffRepository academicStaffRepository, InitialTraineeInformationFormRepository initialTraineeInformationFormRepository, CompanyBranchRepository companyBranchRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.academicStaffRepository = academicStaffRepository;
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
        this.companyBranchRepository = companyBranchRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> addNewTraineeForm(@RequestBody Map<String, String> payload) {
        try{
            String type= payload.get("type");
            String code= payload.get("code");
            String semester= payload.get("semester") ;
            Boolean health_insurance = Boolean.parseBoolean(payload.get("health_insurance"));
            String company_user_name= payload.get("company_user_name") ;
            String branch_name= payload.get("branch_name") ;
            String company_branch_address= payload.get("company_branch_address") ;
            String company_branch_phone= payload.get("company_branch_phone") ;
            String company_branch_email= payload.get("company_branch_email") ;
            String company_branch_country= payload.get("company_branch_country") ;
            String company_branch_city= payload.get("company_branch_city") ;
            String company_branch_district= payload.get("company_branch_district") ;
            String startDate= payload.get("startDate") ;
            String endDate= payload.get("endDate") ;
            LocalDate start_date = LocalDate.parse(startDate);
            LocalDate end_date = LocalDate.parse(endDate);

            if(Objects.equals(company_branch_email, "") && Objects.equals(company_branch_address, "") && Objects.equals(company_branch_phone, "")){
                User user1 = userRepository.findByUserName(branch_name);
                CompanyBranch cp = companyBranchRepository.findBybranchUserName(user1);
                company_branch_address = cp.getAddress();
                company_branch_phone = cp.getPhone();
                company_branch_email = cp.getBranchEmail();
                company_branch_country = cp.getCountry();
                company_branch_city = cp.getCity();
                company_branch_district = cp.getDistrict();
                System.out.println("Suspects: " + company_user_name + "+" + branch_name + "+" +company_branch_address +"+" + company_branch_phone +"+" + company_branch_email);

            }
            String position= payload.get("position") ;
            String fill_user_name= payload.get("fill_user_name") ;
            Student student = studentRepository.findByUserName(fill_user_name).orElseThrow(() -> new IllegalArgumentException("Student not found with username: " + fill_user_name));

            InitialTraineeInformationForm initialTraineeInformationForm = new InitialTraineeInformationForm();
            initialTraineeInformationForm.setFillUserName(student);
            initialTraineeInformationForm.setType(type);
            initialTraineeInformationForm.setCode(code);
            initialTraineeInformationForm.setSemester(semester);
            initialTraineeInformationForm.setHealthInsurance(health_insurance);
            initialTraineeInformationForm.setCompanyUserName(company_user_name);
            initialTraineeInformationForm.setCompanyBranchAddress(company_branch_address);
            initialTraineeInformationForm.setCompanyBranchPhone(company_branch_phone);
            initialTraineeInformationForm.setCompanyBranchEmail(company_branch_email);
            initialTraineeInformationForm.setCity(company_branch_city);
            initialTraineeInformationForm.setDistrict(company_branch_district);
            initialTraineeInformationForm.setCountry(company_branch_country);
            initialTraineeInformationForm.setPosition(position);
            initialTraineeInformationForm.setDatetime(Instant.now());
            initialTraineeInformationForm.setStatus("WaitingForCoordinatorApproval");
            initialTraineeInformationForm.setBranchName(branch_name);
            initialTraineeInformationForm.setInternshipStartDate(start_date);
            initialTraineeInformationForm.setInternshipEndDate(end_date);
            String supervisor_name = payload.get("supervisor_name");
            String supervisor_surname = payload.get("supervisor_surname");

            initialTraineeInformationForm.setSupervisorName(supervisor_name);
            initialTraineeInformationForm.setSupervisorSurname(supervisor_surname);
            List<User> coordinators = userRepository.findAllByUserType("coordinator");
            Random random = new Random();
            int randomIndex = random.nextInt(coordinators.size());
            User coordinator = coordinators.get(randomIndex);
            AcademicStaff academicStaff = academicStaffRepository.findByUserName(coordinator.getUserName()).orElseThrow(() -> new IllegalArgumentException("Coordinator not found with username: " + coordinator.getUserName()));
            initialTraineeInformationForm.setEvaluateUserName(academicStaff);

            initialTraineeInformationFormRepository.save(initialTraineeInformationForm);

            List<Object> response = new ArrayList<>();
            response.add("Trainee form created successfully");
            response.add(initialTraineeInformationForm);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            List<Object> errorResponse = new ArrayList<>();
            errorResponse.add("Error creating trainee form");
            errorResponse.add(e.getMessage());

            return ResponseEntity.status(400).body(errorResponse);
        }

    }
}
