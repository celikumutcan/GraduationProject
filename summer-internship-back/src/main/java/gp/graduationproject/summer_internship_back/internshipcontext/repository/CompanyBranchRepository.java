package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing company branch data.
 */
public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Integer> {

    /**
     * Retrieves all company branches associated with a given company username.
     *
     * @param userName The username of the company
     * @return List of company branches
     */
    List<CompanyBranch> findAllByCompanyUserName_UserName(String userName);

    /**
     * Finds a company branch by its branch user.
     *
     * @param branchUserName The user entity representing the branch
     * @return Optional containing the company branch if found
     */
    Optional<CompanyBranch> findByBranchUserName(User branchUserName);
}