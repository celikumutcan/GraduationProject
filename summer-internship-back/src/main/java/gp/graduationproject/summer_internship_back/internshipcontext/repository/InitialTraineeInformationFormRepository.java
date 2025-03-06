package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing InitialTraineeInformationForm entities.
 * Provides methods to retrieve trainee forms based on various criteria.
 */
public interface InitialTraineeInformationFormRepository extends JpaRepository<InitialTraineeInformationForm, Integer> {

    /**
     * Retrieves all trainee forms associated with a specific student.
     *
     * @param userName The username of the student.
     * @return A list of InitialTraineeInformationForm instances.
     */
    List<InitialTraineeInformationForm> findAllByFillUserName_UserName(String userName);

    /**
     * Finds a trainee form by student, internship end date, and position.
     *
     * @param fillUserName The student associated with the form.
     * @param internshipEndDate The end date of the internship.
     * @param position The position applied for in the internship.
     * @return An optional InitialTraineeInformationForm if found.
     */
    Optional<InitialTraineeInformationForm> findByFillUserNameAndInternshipEndDateAndPosition(
            Student fillUserName, LocalDate internshipEndDate, String position);

    /**
     * Finds a trainee form by its ID.
     *
     * @param id The ID of the trainee form.
     * @return An Optional containing the trainee form if found.
     */
    @NonNull
    Optional<InitialTraineeInformationForm> findById(Integer id);

    @Modifying
    @Query("UPDATE InitialTraineeInformationForm f SET f.status = :status WHERE f.id = :id")
    void updateStatus(@Param("id") Integer id, @Param("status") String status);
}