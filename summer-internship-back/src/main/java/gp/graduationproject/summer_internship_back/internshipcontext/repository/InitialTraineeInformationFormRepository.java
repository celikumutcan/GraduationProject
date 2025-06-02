package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO;
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

    /**
     * Retrieves all initial trainee forms with essential data for a specific student as DTOs.
     *
     * @param username the username of the student
     * @return list of InitialTraineeInformationFormDTO objects
     */
    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO(" +
            "f.id, s.users.firstName, s.users.lastName, s.userName, f.datetime, f.position, f.type, f.code, f.semester, " +
            "f.supervisorName, f.supervisorSurname, f.healthInsurance, f.status, f.companyUserName, f.branchName, " +
            "f.companyBranchAddress, f.companyBranchPhone, f.companyBranchEmail, f.country, f.city, f.district, " +
            "f.internshipStartDate, f.internshipEndDate, " +
            "COALESCE(f.coordinatorUserName.userName, 'Unknown'), COALESCE(f.evaluatingFacultyMember, 'Unknown')) " +
            "FROM InitialTraineeInformationForm f " +
            "JOIN f.fillUserName s " +
            "WHERE s.userName = :username")
    List<InitialTraineeInformationFormDTO> findAllInitialFormDTOsByStudentUsername(@Param("username") String username);

    /**
     * Returns all initial trainee forms with only selected basic fields to improve performance.
     */
    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO(" +
            "i.id, u.firstName, u.lastName, s.userName, i.datetime, i.position, i.type, i.code, i.semester, " +
            "i.supervisorName, i.supervisorSurname, i.healthInsurance, i.status, i.companyUserName, " +
            "i.branchName, i.companyBranchAddress, i.companyBranchPhone, i.companyBranchEmail, " +
            "i.country, i.city, i.district, i.internshipStartDate, i.internshipEndDate, " +
            "coord.userName, i.evaluatingFacultyMember) " +
            "FROM InitialTraineeInformationForm i " +
            "JOIN i.fillUserName s " +
            "JOIN s.users u " +
            "LEFT JOIN i.coordinatorUserName coord")
    List<InitialTraineeInformationFormDTO> findAllInitialTraineeInformationFormDTOs();

}