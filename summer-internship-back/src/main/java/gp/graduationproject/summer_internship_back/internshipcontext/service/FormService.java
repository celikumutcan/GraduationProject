package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Form;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.FormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.FormDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class FormService {

    private final FormRepository formRepository;
    private final AcademicStaffRepository academicStaffRepository;

    /**
     * Constructs a new FormService with the given repositories.
     *
     * @param academicStaffRepository The repository to manage AcademicStaff entities.
     * @param formRepository          The repository to manage Form entities.
     */
    public FormService(AcademicStaffRepository academicStaffRepository, FormRepository formRepository)
    {
        this.academicStaffRepository = academicStaffRepository;
        this.formRepository = formRepository;
    }

    /**
     * Add a new form to the database.
     *
     * @param file        The file content to be stored.
     * @param content     Additional content for the form.
     * @param addUserName The username of the academic staff adding the form.
     * @return The saved form entity.
     */
    public Form addForm(byte[] file, String content, String addUserName)
    {
        AcademicStaff academicStaff;
        try {
            academicStaff = academicStaffRepository.findById(addUserName).get();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("AcademicStaff not found with username: " + addUserName);
        }

        Form form = new Form();
        form.setDatetime(Instant.now());
        form.setFile(file);
        form.setContent(content);
        form.setAddUserName(academicStaff);

        return formRepository.save(form);
    }

    /**
     * Retrieve all forms as FormDTO directly from the repository.
     *
     * @return A list of all forms as FormDTOs.
     */
    public List<FormDTO> getAllFormsDTO()
    {
        return formRepository.findAllFormDTOs();
    }


    /**
     * Delete a form by its ID.
     *
     * @param id The ID of the form to delete.
     */
    public void deleteForm(Integer id)
    {
        if (!formRepository.existsById(id))
        {
            throw new RuntimeException("Form not found with ID: " + id);
        }
        formRepository.deleteById(id);
    }

    /**
     * Find a form by its ID.
     *
     * @param id The ID of the form.
     * @return The Form entity.
     */
    public Form getFormById(Integer id)
    {
        return formRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form not found with ID: " + id));
    }
}