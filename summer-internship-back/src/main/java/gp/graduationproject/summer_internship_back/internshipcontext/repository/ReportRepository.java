package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    /**
     * Retrieves all reports for a given Approved Trainee Information Form ID.
     *
     * @param traineeFormId the trainee information form ID
     * @return a list of reports
     */
    List<Report> findAllByTraineeInformationForm_Id(Integer traineeFormId);

    /**
     * Finds reports within a specific date range for forms assigned to an instructor.
     *
     * @param instructorUserName The instructor's username.
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return List of reports matching the criteria.
     */
    @Query("SELECT r FROM Report r " +
            "WHERE r.traineeInformationForm.coordinatorUserName.userName = :instructorUserName " +
            "AND r.createdAt BETWEEN :startDate AND :endDate")
    List<Report> findReportsByInstructorAndDateRange(
            @Param("instructorUserName") String instructorUserName,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    /**
     * Retrieves a list of ReportDTOs by trainee information form ID.
     * This query only selects basic fields and ignores file data for faster loading.
     *
     * @param traineeInformationFormId the ID of the trainee information form
     * @return a list of ReportDTOs
     */
    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO(" +
            "r.id, r.traineeInformationForm.id, null, r.grade, r.feedback, r.status, null, r.createdAt) " +
            "FROM Report r WHERE r.traineeInformationForm.id = :traineeInformationFormId")
    List<ReportDTO> findReportDTOsByTraineeInformationFormId(@Param("traineeInformationFormId") Integer traineeInformationFormId);

    @Query("""
    SELECT r FROM Report r
    JOIN FETCH r.traineeInformationForm tif
    JOIN FETCH tif.fillUserName fu
    JOIN FETCH fu.users u
    WHERE r.id = :id
""")
    Optional<Report> findReportWithStudent(@Param("id") Integer id);
}