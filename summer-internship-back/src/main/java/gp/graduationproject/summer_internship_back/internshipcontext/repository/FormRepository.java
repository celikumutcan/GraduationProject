package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Form;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.FormDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FormRepository extends JpaRepository<Form, Integer> {

    /**
     * Find all forms and project them directly into FormDTO without file content.
     *
     * @return List of FormDTOs.
     */
    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.FormDTO(f.id, f.datetime, CONCAT(a.users.firstName, ' ', a.users.lastName)) " +
            "FROM Form f JOIN f.addUserName a JOIN a.users")
    List<FormDTO> findAllFormDTOs();
}
