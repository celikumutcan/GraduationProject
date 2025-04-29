package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
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

    private final Map<String, String> studentAssignments = new HashMap<>();

    @PostMapping("/students-to-instructors")
    public List<Map<String, String>> assignStudentsToInstructors(@RequestBody Map<String, List<String>> requestData) {
        List<String> instructors = requestData.get("instructors");

        if (instructors == null || instructors.isEmpty()) {
            throw new IllegalArgumentException("Instructors list must not be empty.");
        }

        List<ApprovedTraineeInformationForm> approvedStudents = approvedTraineeInformationFormService
                .getApprovedTraineeInformationForms()
                .stream()
                .filter(form -> "Approved".equals(form.getStatus()))
                .filter(form -> "defaultEvaluator".equals(form.getEvaluatingFacultyMember()))
                .collect(Collectors.toList());

        if (approvedStudents.isEmpty()) {
            throw new IllegalArgumentException("No approved internship students with defaultEvaluator found.");
        }

        List<Map<String, String>> studentAssignments = new ArrayList<>();

        int instructorCount = instructors.size();
        int studentCount = approvedStudents.size();
        int studentsPerInstructor = studentCount / instructorCount;
        int extraStudents = studentCount % instructorCount;

        int studentIndex = 0;
        for (int i = 0; i < instructorCount; i++) {
            String instructor = instructors.get(i);

            for (int j = 0; j < studentsPerInstructor; j++) {
                if (studentIndex < approvedStudents.size()) {
                    ApprovedTraineeInformationForm studentForm = approvedStudents.get(studentIndex++);
                    studentForm.setEvaluatingFacultyMember(instructor);
                    Map<String, String> assignment = new HashMap<>();
                    assignment.put("student", studentForm.getFillUserName().getUserName());
                    assignment.put("assignedInstructor", instructor);
                    studentAssignments.add(assignment);
                }
            }
        }

        for (int i = 0; i < extraStudents; i++) {
            if (studentIndex < approvedStudents.size()) {
                ApprovedTraineeInformationForm studentForm = approvedStudents.get(studentIndex++);
                String instructor = instructors.get(i);
                studentForm.setEvaluatingFacultyMember(instructor);
                Map<String, String> assignment = new HashMap<>();
                assignment.put("student", studentForm.getFillUserName().getUserName());
                assignment.put("assignedInstructor", instructor);
                studentAssignments.add(assignment);
            }
        }

        approvedTraineeInformationFormService.saveAll(approvedStudents);

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

