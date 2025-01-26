package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Announcement;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Company;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AnnouncementRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyBranchService {

    private final CompanyBranchRepository companyBranchRepository;

    public CompanyBranchService(CompanyBranchRepository companyBranchRepository) {
        this.companyBranchRepository = companyBranchRepository;
    }

    public CompanyBranch saveCompanyBranch(CompanyBranch companyBranch) {
        return companyBranchRepository.save(companyBranch);
    }

    public List<CompanyBranch> getAllCompanyBranchesofCompany(String userName) {
        return companyBranchRepository.findAllByCompanyUserName_UserName(userName);
    }

    public Optional<CompanyBranch> getCompanyBranchById(Integer id) {
        return companyBranchRepository.findById(id);
    }

    public void deleteCompany(Integer id) {
        companyBranchRepository.deleteById(id);
    }
}


