package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.RandomlyAssignInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
public class RandomlyAssignInstructorController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    @Autowired
    private UserRepository userRepository;
    private RandomlyAssignInstructorService randomlyAssignInstructorService;


    private final Map<String, String> studentAssignments = new HashMap<>();

    @PostMapping("/assign-manually")
    public Map<String, String> assignInstructorManually(@RequestBody Map<String, String> request) {
        String studentUsername = request.get("studentUsername");
        String instructorUsername = request.get("instructorUsername");

        boolean success = randomlyAssignInstructorService.assignInstructorManually(studentUsername, instructorUsername);

        if (success) {
            return Map.of("message", "Instructor assigned successfully");
        } else {
            return Map.of("message", "Student not found or assignment failed");
        }
    }

    @PostMapping("/students-to-instructors")
    public List<Map<String, String>> assignStudentsToInstructors(@RequestBody Map<String, List<String>> requestData) {
        List<String> instructors = requestData.get("instructors");

        if (instructors == null || instructors.isEmpty()) {
            throw new IllegalArgumentException("Instructors list must not be empty.");
        }

        return randomlyAssignInstructorService.assignStudentsToInstructors(instructors);
    }

    private void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}

