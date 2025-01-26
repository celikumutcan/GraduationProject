package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Integer> {
    List<CompanyBranch> findAllByCompanyUserName_UserName(String userName);
    CompanyBranch findBybranchUserName(User userName);
}
