package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Announcement;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InitialTraineeInformationFormService {
    @Autowired
    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;
    @Autowired
    private StudentRepository studentRepository;

    public InitialTraineeInformationFormService(InitialTraineeInformationFormRepository initialTraineeInformationFormRepository) {
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
    }

    public List<InitialTraineeInformationForm> getAllInitialTraineeInformationFormofStudent(String userName) {
        studentRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("Student not found"));

        return initialTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    public List<InitialTraineeInformationForm> getInitialTraineeInformationForms() {
        return initialTraineeInformationFormRepository.findAll();
    }
}
