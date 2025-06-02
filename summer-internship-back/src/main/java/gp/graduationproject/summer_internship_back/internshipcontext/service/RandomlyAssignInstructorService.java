package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RandomlyAssignInstructorService {

    @Autowired
    private ApprovedTraineeInformationFormRepository formRepository;

    public void saveAll(List<ApprovedTraineeInformationForm> forms) {
        formRepository.saveAll(forms);
    }

    public boolean assignInstructorManually(Integer formId, String instructorUserName) {
        Optional<ApprovedTraineeInformationForm> formOptional = formRepository.findByid(formId);

        if (formOptional.isPresent()) {
            ApprovedTraineeInformationForm form = formOptional.get();
            form.setEvaluatingFacultyMember(instructorUserName);
            formRepository.save(form);
            return true;
        }

        return false;

    }
    public List<Map<String, Object>> assignStudentsToInstructors(List<Integer> formIds, List<String> instructors) {
        List<ApprovedTraineeInformationForm> forms = formRepository.findByIdIn(formIds);

        if (forms.isEmpty()) {
            throw new IllegalArgumentException("No approved internship forms with defaultEvaluator found.");
        }

        List<Map<String, String>> studentAssignments = new ArrayList<>();
        int instructorCount = instructors.size();
        int studentCount = forms.size();
        int studentsPerInstructor = studentCount / instructorCount;
        int extraStudents = studentCount % instructorCount;

        int studentIndex = 0;
        for (int i = 0; i < instructorCount; i++) {
            String instructor = instructors.get(i);
            for (int j = 0; j < studentsPerInstructor; j++) {
                if (studentIndex < studentCount) {
                    ApprovedTraineeInformationForm form = forms.get(studentIndex++);
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
                ApprovedTraineeInformationForm form = forms.get(studentIndex++);
                String instructor = instructors.get(i);
                form.setEvaluatingFacultyMember(instructor);
                studentAssignments.add(Map.of(
                        "student", form.getFillUserName().getUserName(),
                        "assignedInstructor", instructor
                ));
            }
        }

        saveAll(forms);
        List<Map<String, Object>> castedAssignments = new ArrayList<>();
        for (Map<String, String> assignment : studentAssignments) {
            Map<String, Object> casted = new HashMap<>(assignment);
            castedAssignments.add(casted);
        }
        return castedAssignments;

    }
}