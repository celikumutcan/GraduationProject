package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByTraineeInformationForm_Id(Integer traineeFormId);
    List<Report> findAllByTraineeInformationForm_IdAndStatus(Integer traineeFormId, String status);
}