package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    /**
     * Retrieves an InitialTraineeInformationForm by ID.
     *
     * @param id The ID of the trainee form.
     * @return Optional containing the trainee form if found.
     */
    public Optional<InitialTraineeInformationForm> getInitialTraineeInformationFormById(Integer id) {
        return initialTraineeInformationFormRepository.findById(id);
    }


}