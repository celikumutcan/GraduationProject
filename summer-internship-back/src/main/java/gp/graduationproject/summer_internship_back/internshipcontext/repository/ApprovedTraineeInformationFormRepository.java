package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovedTraineeInformationFormRepository extends JpaRepository<ApprovedTraineeInformationForm, Integer> {
    List<ApprovedTraineeInformationForm> findAllByFillUserName_UserName(String userName);
    List<ApprovedTraineeInformationForm> findAllByEvaluateUserName_UserName(String userName);
    List<ApprovedTraineeInformationForm> findAllByCompanyBranch_Id(Integer companyBranchId);
}
