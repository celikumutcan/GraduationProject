package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ApprovedTraineeInformationFormRepository extends JpaRepository<ApprovedTraineeInformationForm, Integer> {

    List<ApprovedTraineeInformationForm> findAllByFillUserName_UserName(@NonNull String userName);

    List<ApprovedTraineeInformationForm> findAllByCoordinatorUserName_UserName(@NonNull String userName);

    List<ApprovedTraineeInformationForm> findAllByCompanyBranch_Id(@NonNull Integer companyBranchId);

    Optional<ApprovedTraineeInformationForm> findByid(@NonNull Integer id);

    Optional<ApprovedTraineeInformationForm> findTopByFillUserName_UserNameOrderByIdDesc(String fillUserName);

    // 📌 Öğrencinin CV'sindeki kelimeleri içeren tüm position'ları getir
    @Query("SELECT DISTINCT a.position FROM ApprovedTraineeInformationForm a WHERE LOWER(a.position) LIKE %:keyword%")
    List<String> findPositionsByKeyword(@Param("keyword") String keyword);


    @Query("SELECT DISTINCT at.position FROM ApprovedTraineeInformationForm at")
    List<String> findAllPositions();


}