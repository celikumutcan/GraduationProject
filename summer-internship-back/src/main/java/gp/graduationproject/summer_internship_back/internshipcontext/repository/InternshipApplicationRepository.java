package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for InternshipApplication.
 * Provides methods to interact with the database.
 */
@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {

    /**
     * Finds all internship applications for a specific student by username.
     * @param studentUserName The username of the student.
     * @return List of internship applications submitted by the student.
     */
    List<InternshipApplication> findByStudent_UserName(String studentUserName);

    /**
     * Finds all internship applications submitted to a specific company branch.
     * @param companyBranchId The ID of the company branch.
     * @return List of internship applications submitted to the branch.
     */
    List<InternshipApplication> findByCompanyBranch_Id(Integer companyBranchId);
}
