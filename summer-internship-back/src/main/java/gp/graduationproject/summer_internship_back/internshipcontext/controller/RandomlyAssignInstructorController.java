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
    @Autowired
    private RandomlyAssignInstructorService randomlyAssignInstructorService;


    private final Map<String, String> studentAssignments = new HashMap<>();

    @PostMapping("/assign-manually")
    public Map<String, String> assignInstructorManually(@RequestBody Map<String, String> request) {

        Integer formId = Integer.valueOf(request.get("id"));
        String instructorUsername = request.get("instructorUsername");

        boolean success = randomlyAssignInstructorService.assignInstructorManually(formId, instructorUsername);

        if (success) {
            return Map.of("message", "Instructor assigned successfully");
        } else {
            return Map.of("message", "Student not found or assignment failed");
        }
    }

    @PostMapping("/students-to-instructors")
    public List<Map<String, Object>> assignStudentsToInstructors(@RequestBody Map<String, Object> requestData) {
        List<Integer> formIds = (List<Integer>) requestData.get("assignFormIds");
        List<String> instructors = (List<String>) requestData.get("instructors");

        if (instructors == null || instructors.isEmpty()) {
            throw new IllegalArgumentException("Instructors list must not be empty.");
        }
        List<Map<String, Object>> assignments = randomlyAssignInstructorService.assignStudentsToInstructors(formIds, instructors);

        // Bildirim gönderilecek map
        Map<String, List<String>> groupedByInstructor = new HashMap<>();
        for (Map<String, Object> assignment : assignments) {
            String student = (String) assignment.get("student");
            String instructor = (String) assignment.get("assignedInstructor");
            groupedByInstructor.computeIfAbsent(instructor, k -> new ArrayList<>()).add(student);
        }

        for (Map.Entry<String, List<String>> entry : groupedByInstructor.entrySet()) {
            String instructorUsername = entry.getKey();
            List<String> students = entry.getValue();

            User user= userRepository.findByUserName(instructorUsername);
            if (user != null && user.getEmail() != null) {
                String email = user.getEmail();
                StringBuilder body = new StringBuilder("Dear Instructor,\n\nThe following students have been assigned to you:\n\n");
                for (String student : students) {
                    body.append("- ").append(student).append("\n");
                }
                body.append("\nPlease log in to the system for details.\nBest regards,\nInternship Management System");
                sendEmail(email, "📩 New Internship Student Assignments", body.toString());
            }

            for (String studentUsername : students) {
                User student = userRepository.findByUserName(studentUsername);
                if (student != null && student.getEmail() != null) {
                    String email = student.getEmail();
                    StringBuilder body = new StringBuilder("Dear Student,\n\nYou have been assigned to instructor: " + instructorUsername + ".\n");
                    body.append("Please contact your instructor if you have any questions.\n")
                            .append("You can view the details in the system.\n\n")
                            .append("Best regards,\nInternship Management System");
                    sendEmail(email, "📢 Your Internship Instructor is Assigned", body.toString());
                }
            }
        }

        return assignments;
    }

    private void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}

