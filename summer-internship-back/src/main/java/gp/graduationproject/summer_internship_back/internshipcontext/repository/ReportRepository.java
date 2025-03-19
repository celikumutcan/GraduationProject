package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
}