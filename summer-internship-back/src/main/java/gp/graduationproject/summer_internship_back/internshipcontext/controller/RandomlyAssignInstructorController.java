package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

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

    private final Map<String, String> studentAssignments = new HashMap<>();

    @PostMapping("/students-to-instructors")
    public Map<String, String> assignStudentsToInstructors(@RequestBody Map<String, List<String>> requestData) {
        List<String> instructors = requestData.get("instructors");

        if (instructors == null || instructors.isEmpty()) {
            throw new IllegalArgumentException("Instructors list must not be empty.");
        }

        // ✅ Approved Internship Form içindeki öğrencileri çek
        List<ApprovedTraineeInformationForm> approvedStudents = approvedTraineeInformationFormService.getApprovedTraineeInformationForms();
        List<String> students = approvedStudents.stream()
                .map(form -> form.getFillUserName().getUserName())
                .collect(Collectors.toList());

        if (students.isEmpty()) {
            throw new IllegalArgumentException("No approved internship students found.");
        }

        studentAssignments.clear();

        int instructorCount = instructors.size();
        int studentCount = students.size();
        int studentsPerInstructor = studentCount / instructorCount;
        int extraStudents = studentCount % instructorCount;

        int studentIndex = 0;
        for (int i = 0; i < instructorCount; i++) {
            String instructor = instructors.get(i);

            for (int j = 0; j < studentsPerInstructor; j++) {
                if (studentIndex < students.size()) {
                    String student = students.get(studentIndex++);
                    studentAssignments.put(student, instructor);
                    sendEmail(student, "Instructor Assigned", "You have been assigned to " + instructor);
                }
            }
        }

        for (int i = 0; i < extraStudents; i++) {
            if (studentIndex < students.size()) {
                String student = students.get(studentIndex++);
                String instructor = instructors.get(i);
                studentAssignments.put(student, instructor);
                sendEmail(student, "Instructor Assigned", "You have been assigned to " + instructor);
            }
        }

        for (String instructor : instructors) {
            List<String> assignedStudents = new ArrayList<>();
            for (Map.Entry<String, String> entry : studentAssignments.entrySet()) {
                if (entry.getValue().equals(instructor)) {
                    assignedStudents.add(entry.getKey());
                }
            }
            if (!assignedStudents.isEmpty()) {
                sendEmail(instructor, "Assigned Students", "The following students have been assigned to you: " + assignedStudents);
            }
        }

        return studentAssignments;
    }

    private void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
