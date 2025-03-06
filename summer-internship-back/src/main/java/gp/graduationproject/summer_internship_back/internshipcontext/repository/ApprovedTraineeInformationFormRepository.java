package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ApprovedTraineeInformationFormRepository extends JpaRepository<ApprovedTraineeInformationForm, Integer> {

    List<ApprovedTraineeInformationForm> findAllByFillUserName_UserName(@NonNull String userName);

    List<ApprovedTraineeInformationForm> findAllByCoordinatorUserName_UserName(@NonNull String userName);

    List<ApprovedTraineeInformationForm> findAllByCompanyBranch_Id(@NonNull Integer companyBranchId);

    Optional<ApprovedTraineeInformationForm> findByid(@NonNull Integer id);

    Optional<ApprovedTraineeInformationForm> findTopByFillUserName_UserNameOrderByIdDesc(String fillUserName);
}