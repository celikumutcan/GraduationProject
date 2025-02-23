package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/assignments")
public class RandomlyAssignInstructorController {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> studentAssignments = new HashMap<>(); // Store assignments

    @PostMapping("/students-to-instructors")
    public Map<String, String> assignStudentsToInstructors(@RequestBody Map<String, List<String>> requestData) {
        List<String> students = requestData.get("students");
        List<String> instructors = requestData.get("instructors");

        if (students == null || students.isEmpty() || instructors == null || instructors.isEmpty()) {
            throw new IllegalArgumentException("Students and instructors lists must not be empty.");
        }

        studentAssignments.clear(); // Reset previous assignments

        int instructorCount = instructors.size();
        int studentCount = students.size();
        int studentsPerInstructor = studentCount / instructorCount;
        int extraStudents = studentCount % instructorCount;

        int studentIndex = 0;
        for (int i = 0; i < instructorCount; i++) {
            String instructor = instructors.get(i);

            // Assign the calculated number of students to this instructor
            for (int j = 0; j < studentsPerInstructor; j++) {
                if (studentIndex < students.size()) {
                    String student = students.get(studentIndex++);
                    studentAssignments.put(student, instructor);
                    sendEmail(student, "Instructor Assigned", "You have been assigned to " + instructor);
                }
            }
        }

        // Distribute extra students to instructors
        for (int i = 0; i < extraStudents; i++) {
            if (studentIndex < students.size()) {
                String student = students.get(studentIndex++);
                String instructor = instructors.get(i); // Assign extra students to the first few instructors
                studentAssignments.put(student, instructor);
                sendEmail(student, "Instructor Assigned", "You have been assigned to " + instructor);
            }
        }

        // Send summary email to each instructor
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
