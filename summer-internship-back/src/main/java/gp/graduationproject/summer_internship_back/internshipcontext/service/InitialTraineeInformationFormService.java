package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import jakarta.transaction.Transactional;
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
            StudentRepository studentRepository)
    {
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
    public List<InitialTraineeInformationForm> getAllInitialTraineeInformationFormOfStudent(String userName)
    {
        studentRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return initialTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    /**
     * Retrieves all initial trainee information forms from the database.
     *
     * @return A list of all trainee information forms.
     */
    public List<InitialTraineeInformationForm> getInitialTraineeInformationForms()
    {
        return initialTraineeInformationFormRepository.findAll();
    }

    /**
     * Retrieves an InitialTraineeInformationForm by ID.
     *
     * @param id The ID of the trainee form.
     * @return Optional containing the trainee form if found.
     */
    public Optional<InitialTraineeInformationForm> getInitialTraineeInformationFormById(Integer id)
    {
        return initialTraineeInformationFormRepository.findById(id);
    }

<<<<<<< HEAD
=======
    /**
     * Deletes an InitialTraineeInformationForm record if the given user is authorized.
     * The deletion is only allowed if the provided username matches the form owner's username.
     *
     * @param id       The ID of the trainee form to be deleted.
     * @param username The username of the student attempting to delete the form.
     * @return {@code true} if the form is successfully deleted, {@code false} otherwise.
     */
    @Transactional
    public boolean deleteInitialTraineeInformationForm(Integer id, String username) {
        Optional<InitialTraineeInformationForm> form = initialTraineeInformationFormRepository.findById(id);

        // Check if the form exists and belongs to the given username
        if (form.isPresent() && form.get().getFillUserName().getUserName().equals(username)) {
            initialTraineeInformationFormRepository.deleteById(id);
            return true;
        }

        return false;
    }

    /**
     * Updates an existing initial trainee information form if the user is authorized.
     *
     * @param id          The ID of the trainee form to be updated.
     * @param username    The username of the student attempting to update the form.
     * @param updatedForm The new form data containing the updated values.
     * @return The updated InitialTraineeInformationForm object.
     * @throws RuntimeException If the form is not found or if the user is not authorized to update it.
     */
    @Transactional
    public InitialTraineeInformationForm updateInitialTraineeInformationForm(Integer id, String username, InitialTraineeInformationForm updatedForm)
    {
        // Check if form exists
        InitialTraineeInformationForm form = initialTraineeInformationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Form not found! ID: " + id));

        // Check if the user is the owner
        if (!form.getFillUserName().getUserName().equals(username)) {
            throw new RuntimeException("Unauthorized action! User " + username + " is not the owner of this form.");
        }

        // Update form fields
        form.setPosition(updatedForm.getPosition());
        form.setType(updatedForm.getType());
        form.setCode(updatedForm.getCode());
        form.setSemester(updatedForm.getSemester());
        form.setSupervisorName(updatedForm.getSupervisorName());
        form.setSupervisorSurname(updatedForm.getSupervisorSurname());
        form.setHealthInsurance(updatedForm.getHealthInsurance());
        form.setStatus(updatedForm.getStatus());
        form.setCompanyUserName(updatedForm.getCompanyUserName());
        form.setBranchName(updatedForm.getBranchName());
        form.setCompanyBranchAddress(updatedForm.getCompanyBranchAddress());
        form.setCompanyBranchPhone(updatedForm.getCompanyBranchPhone());
        form.setCompanyBranchEmail(updatedForm.getCompanyBranchEmail());
        form.setInternshipStartDate(updatedForm.getInternshipStartDate());
        form.setInternshipEndDate(updatedForm.getInternshipEndDate());
        form.setCountry(updatedForm.getCountry());
        form.setCity(updatedForm.getCity());
        form.setDistrict(updatedForm.getDistrict());

        return initialTraineeInformationFormRepository.save(form);
    }
>>>>>>> 9ac127ae1d8b6a0e4ac5c9148a9b303997b8db9b

}