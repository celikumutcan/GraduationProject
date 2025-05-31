package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApprovedTraineeInformationFormRepository extends JpaRepository<ApprovedTraineeInformationForm, Integer> {

    List<ApprovedTraineeInformationForm> findAllByFillUserName_UserName(@NonNull String userName);


    List<ApprovedTraineeInformationForm> findAllByCompanyBranch_Id(@NonNull Integer companyBranchId);

    Optional<ApprovedTraineeInformationForm> findByid(@NonNull Integer id);

    List<ApprovedTraineeInformationForm> findByIdIn(List<Integer> ids);

    Optional<ApprovedTraineeInformationForm> findTopByFillUserName_UserNameOrderByIdDesc(String fillUserName);


    @Query("SELECT DISTINCT at.position FROM ApprovedTraineeInformationForm at")
    List<String> findAllPositions();


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


    /**
     * Retrieves a list of Approved Trainee Forms for the given student username
     * using DTO projection to improve performance.
     *
     * @param username The username of the student
     * @return List of ApprovedTraineeInformationFormDTO
     */
    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO(" +
            "a.id, s.users.firstName, s.users.lastName, s.userName, a.datetime, a.position, a.type, a.code, a.semester, " +
            "a.supervisorName, a.supervisorSurname, a.healthInsurance, a.insuranceApproval, a.insuranceApprovalDate, a.status, " +
            "cb.companyUserName.userName, cb.branchName, cb.address, cb.phone, cb.branchEmail, cb.country, cb.city, cb.district, " +
            "c.userName, a.evaluatingFacultyMember, a.internshipStartDate, a.internshipEndDate" +
            ") " +
            "FROM ApprovedTraineeInformationForm a " +
            "JOIN a.fillUserName s " +
            "JOIN a.companyBranch cb " +
            "JOIN a.coordinatorUserName c " +
            "WHERE s.userName = :username")
    List<ApprovedTraineeInformationFormDTO> findAllInternshipDTOsByUsername(@Param("username") String username);

    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO(" +
            "a.id, s.users.firstName, s.users.lastName, s.userName, a.datetime, a.position, a.type, a.code, a.semester, " +
            "a.supervisorName, a.supervisorSurname, a.healthInsurance, a.insuranceApproval, a.insuranceApprovalDate, a.status, " +
            "cb.companyUserName.userName, cb.branchName, cb.address, cb.phone, cb.branchEmail, cb.country, cb.city, cb.district, " +
            "c.userName, a.evaluatingFacultyMember, a.internshipStartDate, a.internshipEndDate" +
            ") " +
            "FROM ApprovedTraineeInformationForm a " +
            "JOIN a.fillUserName s " +
            "JOIN a.companyBranch cb " +
            "JOIN a.coordinatorUserName c " +
            "WHERE a.status = 'Approved'")
    List<ApprovedTraineeInformationFormDTO> findApprovedInternshipsForStudentAffairs();


    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormCompanyDTO(" +
            "a.id, s.users.firstName, s.users.lastName, s.userName, a.datetime, a.position, a.type, " +
            "a.supervisorName, a.supervisorSurname, a.healthInsurance, a.insuranceApproval, a.insuranceApprovalDate, a.status, " +
            "cb.companyUserName.userName, cb.branchName," +
            "a.internshipStartDate, a.internshipEndDate" +
            ") " +
            "FROM ApprovedTraineeInformationForm a " +
            "JOIN a.fillUserName s " +
            "JOIN a.companyBranch cb " +
            "JOIN a.coordinatorUserName c " +
            "WHERE cb.branchName = :branchname")
    List<ApprovedTraineeInformationFormCompanyDTO> findApprovedInternshipsForCompanies(@Param("branchname") String branchname);

    /**
     * Returns a list of ReportExportDTO objects for approved trainee forms.
     * Only selected fields are fetched: first name, last name, username, code, and grade.
     * This method is used to generate Excel reports faster and with less memory usage.
     *
     * @param startDate Start date to filter internship forms.
     * @param endDate End date to filter internship forms.
     * @return A list of ReportExportDTO containing basic student and grade information.
     */
    @Query("""
    SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportExportDTO(
        u.firstName, u.lastName, s.userName, a.code, r.grade
    )
    FROM ApprovedTraineeInformationForm a
    JOIN a.fillUserName s
    JOIN s.users u
    LEFT JOIN a.reports r
    WHERE a.datetime BETWEEN :startDate AND :endDate
    ORDER BY a.code
""")
    List<ReportExportDTO> findFormsForExport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.MinimalInternshipDTO(a.position, a.companyBranch) " +
            "FROM ApprovedTraineeInformationForm a WHERE a.id = :id")
    Optional<MinimalInternshipDTO> findMinimalInternshipDTOById(@Param("id") Integer id);


    /**
     * Retrieves only minimal information about a trainee form for performance optimization.
     *
     * @param id the ID of the trainee form
     * @return a projection with fillUserName, status, and evaluatingFacultyMember
     */
    @Query("SELECT f.fillUserName.userName AS fillUserName, f.status AS status, f.evaluatingFacultyMember AS evaluatingFacultyMember " +
            "FROM ApprovedTraineeInformationForm f WHERE f.id = :id")
    Optional<MinimalFormDataDTO> findMinimalFormDataById(@Param("id") Integer id);

}