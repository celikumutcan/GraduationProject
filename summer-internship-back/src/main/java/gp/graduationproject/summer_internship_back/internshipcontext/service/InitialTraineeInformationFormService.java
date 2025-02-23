package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitialTraineeInformationFormService {

    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructor for InitialTraineeInformationFormService.
     * Injects dependencies for handling trainee forms.
     *
     * @param initialTraineeInformationFormRepository Repository for trainee information forms.
     * @param studentRepository Repository for students.
     */
    public InitialTraineeInformationFormService(
            InitialTraineeInformationFormRepository initialTraineeInformationFormRepository,
            StudentRepository studentRepository
    ) {
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves all initial trainee information forms for a specific student.
     *
     * @param userName The username of the student.
     * @return A list of trainee information forms for the given student.
     * @throws RuntimeException if the student is not found.
     */
    public List<InitialTraineeInformationForm> getAllInitialTraineeInformationFormOfStudent(String userName) {
        studentRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return initialTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    /**
     * Retrieves all initial trainee information forms from the database.
     *
     * @return A list of all trainee information forms.
     */
    public List<InitialTraineeInformationForm> getInitialTraineeInformationForms() {
        return initialTraineeInformationFormRepository.findAll();
    }
}