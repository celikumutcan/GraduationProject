package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ReportEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReportEvaluationRepository extends JpaRepository<ReportEvaluation, Integer> {
    List<ReportEvaluation> findAllByReportId(Integer reportId);
    Optional<ReportEvaluation> findByReportAndItemName(Report report, String itemName);


}