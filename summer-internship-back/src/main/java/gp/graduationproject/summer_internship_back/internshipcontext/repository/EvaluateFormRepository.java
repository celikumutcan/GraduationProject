package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluateFormRepository extends JpaRepository<EvaluateForm, Integer> {
    List<EvaluateForm> findAllByTraineeInformationForm_Id(Integer traineeFormId);

}
