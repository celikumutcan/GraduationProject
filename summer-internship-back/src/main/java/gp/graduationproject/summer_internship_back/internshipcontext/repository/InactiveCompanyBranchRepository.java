package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InactiveCompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing inactive company branches.
 */
@Repository
public interface InactiveCompanyBranchRepository extends JpaRepository<InactiveCompanyBranch, Long> {

    /**
     * Finds an inactive company branch by its branch ID.
     *
     * @param branchId the branch ID to search for
     * @return an Optional containing the matching InactiveCompanyBranch, if found
     */
    Optional<InactiveCompanyBranch> findByBranchId(Integer branchId);

    /**
     * Deletes an inactive company branch by its branch ID.
     *
     * @param branchId the branch ID of the branch to delete
     */
    void deleteByBranchId(Integer branchId);
}