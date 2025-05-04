package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RandomlyAssignInstructorService {

    @Autowired
    private ApprovedTraineeInformationFormRepository formRepository;

    public List<ApprovedTraineeInformationForm> getApprovedAndUnassignedForms() {
        return formRepository.findAll().stream()
                .filter(f -> "Approved".equals(f.getStatus()))
                .filter(f -> "defaultEvaluator".equals(f.getEvaluatingFacultyMember()))
                .toList();
    }

    public void saveAll(List<ApprovedTraineeInformationForm> forms) {
        formRepository.saveAll(forms);
    }

    public boolean assignInstructorManually(String studentUserName, String instructorUserName) {
        List<ApprovedTraineeInformationForm> forms = formRepository.findAllByFillUserName_UserName(studentUserName);

        if (!forms.isEmpty()) {
            ApprovedTraineeInformationForm form = forms.get(0);
            form.setEvaluatingFacultyMember(instructorUserName);
            formRepository.save(form);
            return true;
        }

        return false;
    }
    public List<Map<String, String>> assignStudentsToInstructors(List<String> instructors) {
        List<ApprovedTraineeInformationForm> approvedStudents = getApprovedAndUnassignedForms();

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
                if (studentIndex < studentCount) {
                    ApprovedTraineeInformationForm form = approvedStudents.get(studentIndex++);
                    form.setEvaluatingFacultyMember(instructor);
                    studentAssignments.add(Map.of(
                            "student", form.getFillUserName().getUserName(),
                            "assignedInstructor", instructor
                    ));
                }
            }
        }

        for (int i = 0; i < extraStudents; i++) {
            if (studentIndex < studentCount) {
                ApprovedTraineeInformationForm form = approvedStudents.get(studentIndex++);
                String instructor = instructors.get(i);
                form.setEvaluatingFacultyMember(instructor);
                studentAssignments.add(Map.of(
                        "student", form.getFillUserName().getUserName(),
                        "assignedInstructor", instructor
                ));
            }
        }

        saveAll(approvedStudents);
        return studentAssignments;
    }
}