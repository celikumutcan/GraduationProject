package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Company;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Integer> {
    List<CompanyBranch> findAllByCompanyUserName_UserName(String userName);
    Optional<CompanyBranch> findByBranchUserName(User branchUserName);

    /*Buraya eklemek lazım olacak*/
    //Optional<CompanyBranch> findByBranchNameAndCompany(String branchName, Company company);

}