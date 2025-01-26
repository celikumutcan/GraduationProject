package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Form;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for managing {@link Form} entities.
 *
 * This interface extends {@link JpaRepository}, providing CRUD operations
 * and additional query methods for the {@link Form} entity.
 *
 * The {@link JpaRepository} provides ready-to-use methods such as:
 *     <li>save(Form entity) - Saves a form entity to the database</li>
 *     <li>findById(Integer id) - Retrieves a form by its ID</li>
 *     <li>findAll() - Retrieves all form records</li>
 *     <li>deleteById(Integer id) - Deletes a form by its ID</li>
 *
 * @author Umutcan Celik
 * @version 1.0
 */
public interface FormRepository extends JpaRepository<Form, Integer> {
}
