package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluateFormRepository extends JpaRepository<EvaluateForm, Integer> {

    /**
     * Retrieves all evaluation forms related to a specific trainee's internship form.
     *
     * @param traineeInformationForm The approved trainee information form.
     * @return List of evaluation forms.
     */
    List<EvaluateForm> findByTraineeInformationForm(ApprovedTraineeInformationForm traineeInformationForm);
}