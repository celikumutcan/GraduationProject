package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InitialTraineeInformationFormService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/coordinatorApprove")
public class CoordinatorApproveController {

    @Autowired
    private InitialTraineeInformationFormService initialTraineeInformationFormService;

    @Autowired
    private ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;
    private final StudentRepository studentRepository;
    public CoordinatorApproveController(InitialTraineeInformationFormRepository repository, StudentRepository studentRepository) {
        this.initialTraineeInformationFormRepository = repository;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<Object>> addNewTraineeForm(@RequestBody Map<String, String> payload) {
        try {
            // Extract parameters from payload
            String username = payload.get("username");
            String endDate = payload.get("endDate");
            String position = payload.get("position");

            // Validate input
            if (username == null || endDate == null || position == null) {
                return ResponseEntity.badRequest().body(Collections.singletonList("Invalid payload: Missing fields"));
            }

            // Parse the end date
            LocalDate parsedEndDate;
            try {
                parsedEndDate = LocalDate.parse(endDate);
                System.out.println("Enddate: " + parsedEndDate);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(Collections.singletonList("Invalid date format for endDate"));
            }

            // Find the student by username
            Optional<Student> optionalStudent = studentRepository.findByUserName(username);
            if (optionalStudent.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList("No matching student found for username: " + username));
            }

            // Find the trainee form using fillUserName (student), endDate, and position
            Optional<InitialTraineeInformationForm> optionalForm =
                    initialTraineeInformationFormRepository.findByFillUserNameAndInternshipEndDateAndPosition(
                            optionalStudent.get(), parsedEndDate, position);

            if (optionalForm.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList("No matching trainee form found"));
            }

            // Update the status
            InitialTraineeInformationForm form = optionalForm.get();
            form.setStatus("WaitingForCompanyApproval");
            initialTraineeInformationFormRepository.save(form);

            // Prepare response
            List<Object> response = new ArrayList<>();
            response.add("Trainee form status updated successfully");
            response.add(Map.of(
                    "username", username,
                    "endDate", endDate,
                    "position", position,
                    "status", form.getStatus()
            ));

            // Return success response
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("An error occurred while updating the trainee form: " + e.getMessage()));
        }
    }

}