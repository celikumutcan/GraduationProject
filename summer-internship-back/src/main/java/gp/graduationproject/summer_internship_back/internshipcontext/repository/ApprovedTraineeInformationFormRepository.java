package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
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

    @Query("SELECT a FROM ApprovedTraineeInformationForm a LEFT JOIN FETCH a.evaluateForms")
    List<ApprovedTraineeInformationForm> findAllWithEvaluateForms();

    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO(" +
            "a.id, s.users.firstName, s.users.lastName, s.userName, a.datetime, a.position, a.type, a.code, a.semester, " +
            "a.supervisorName, a.supervisorSurname, a.healthInsurance, a.insuranceApproval, a.insuranceApprovalDate, a.status, " +
            "cb.companyUserName.userName, cb.branchName, cb.address, cb.phone, cb.branchEmail, cb.country, cb.city, cb.district, " +
            "c.userName, a.evaluatingFacultyMember, a.internshipStartDate, a.internshipEndDate" +
            ") " +
            "FROM ApprovedTraineeInformationForm a " +
            "JOIN a.fillUserName s " +
            "JOIN a.companyBranch cb " +
            "JOIN a.coordinatorUserName c")
    List<ApprovedTraineeInformationFormDTO> findAllInternshipDTOs();

}