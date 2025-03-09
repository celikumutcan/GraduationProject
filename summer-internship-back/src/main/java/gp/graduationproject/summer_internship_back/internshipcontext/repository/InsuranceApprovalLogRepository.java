package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InsuranceApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing Insurance Approval Log records.
 * This interface extends JpaRepository to provide standard database operations.
 */
@Repository
public interface InsuranceApprovalLogRepository extends JpaRepository<InsuranceApprovalLog, Integer> {

    /**
     * Retrieves a list of insurance approval logs filtered by the approval date.
     *
     * @param startDate The start date of the approval period.
     * @param endDate   The end date of the approval period.
     * @return A list of InsuranceApprovalLog entries within the given date range.
     */
    List<InsuranceApprovalLog> findByApprovalDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}