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
    Optional<InactiveCompanyBranch> findByBranchId(Integer branchId);
}
