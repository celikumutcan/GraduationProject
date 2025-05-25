package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InitialTraineeInformationFormService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * REST Controller for handling trainee form approvals and rejections by the coordinator.
 */
@RestController
@RequestMapping("/api/coordinatorApprove")
public class CoordinatorApproveController {

    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;
    private final StudentRepository studentRepository;
    private final InitialTraineeInformationFormService initialTraineeInformationFormService;
    private final EmailService emailService;

    /**
     * Constructor-based dependency injection.
     *
     * @param initialTraineeInformationFormRepository Repository for trainee forms.
     * @param studentRepository Repository for student entities.
     * @param initialTraineeInformationFormService Service for trainee form business logic.
     * @param emailService Service for sending emails.
     */
    public CoordinatorApproveController(
            InitialTraineeInformationFormRepository initialTraineeInformationFormRepository,
            StudentRepository studentRepository,
            InitialTraineeInformationFormService initialTraineeInformationFormService,
            EmailService emailService
    ) {
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
        this.initialTraineeInformationFormService = initialTraineeInformationFormService;
        this.emailService = emailService;
    }

    /**
     * Approves a trainee form using service logic and updates its status to "Company Approval Waiting".
     *
     * @param id ID of the Initial Trainee Form.
     * @param username Username of the student.
     * @return A response entity with a success or error message.
     */
    @PostMapping("/approve/{id}")
    @Transactional
    public ResponseEntity<List<Object>> approveTraineeForm(@PathVariable Integer id, @RequestParam String username) {
        try {
            boolean success = initialTraineeInformationFormService.updateInitialFormStatus(id, "Company Approval Waiting");

            if (success) {
                return ResponseEntity.ok(Collections.singletonList("Trainee form approved successfully and waiting for company approval."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList("No matching trainee form found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("An error occurred while approving the trainee form: " + e.getMessage()));
        }
    }

    /**
     * Rejects a trainee form and sends an email notification to the student.
     *
     * This method updates the status of the trainee form to "RejectedByCoordinator".
     * It also sends an email to the student if a valid email address is available.
     *
     * @param payload A map containing "username", "endDate", and "position".
     * @return A response entity with a success or error message.
     */
    @PostMapping("/reject")
    @Transactional
    public ResponseEntity<List<Object>> rejectTraineeForm(@RequestBody Map<String, String> payload) {
        try {
            String username = payload.get("username");
            String endDate = payload.get("endDate");
            String position = payload.get("position");

            if (username == null || endDate == null || position == null) {
                return ResponseEntity.badRequest().body(Collections.singletonList("Invalid payload: Missing fields"));
            }

            LocalDate parsedEndDate;
            try {
                parsedEndDate = LocalDate.parse(endDate);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(Collections.singletonList("Invalid date format for endDate"));
            }

            var optionalStudent = studentRepository.findByUserName(username);
            if (optionalStudent.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList("No matching student found for username: " + username));
            }

            var optionalForm = initialTraineeInformationFormRepository.findByFillUserNameAndInternshipEndDateAndPosition(
                    optionalStudent.get(), parsedEndDate, position
            );

            if (optionalForm.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList("No matching trainee form found"));
            }

            InitialTraineeInformationForm form = optionalForm.get();
            form.setStatus("RejectedByCoordinator");
            initialTraineeInformationFormRepository.save(form);

            var user = optionalStudent.get().getUsers();
            if (user != null && user.getEmail() != null && !user.getEmail().isBlank()) {
                emailService.sendRejectionNotificationToStudent(
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName()
                );
            }

            return ResponseEntity.ok(Collections.singletonList("Trainee form rejected by the coordinator."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("An error occurred while rejecting the trainee form: " + e.getMessage()));
        }
    }
}