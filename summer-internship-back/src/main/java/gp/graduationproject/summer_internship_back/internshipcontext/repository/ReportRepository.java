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
     * @param instructorUserName the instructor's username
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return a list of reports
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
     * Retrieves a report with its related trainee form and student data.
     *
     * @param id the ID of the report
     * @return optional report with related entities
     */
    @Query("""
        SELECT r FROM Report r
        JOIN FETCH r.traineeInformationForm tif
        JOIN FETCH tif.fillUserName fu
        JOIN FETCH fu.users u
        WHERE r.id = :id
    """)
    Optional<Report> findReportWithStudent(@Param("id") Integer id);

    /**
     * Retrieves lightweight ReportDTOs for a given form ID.
     *
     * @param formId the ID of the trainee information form
     * @return a list of lightweight ReportDTOs
     */
    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO(" +
            "r.id, r.grade, r.feedback, r.status, r.createdAt) " +
            "FROM Report r WHERE r.traineeInformationForm.id = :formId")
    List<ReportDTO> findLightweightReportDTOsByFormId(@Param("formId") Integer formId);


    /**
     * Counts how many reports are linked to a specific trainee form.
     *
     * @param formId the trainee form ID
     * @return the number of reports
     */
    int countByTraineeInformationFormId(Integer formId);
}