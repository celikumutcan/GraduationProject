package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;

public class MinimalInternshipDTO {
    private String position;
    private CompanyBranch companyBranch;

    public MinimalInternshipDTO(String position, CompanyBranch companyBranch) {
        this.position = position;
        this.companyBranch = companyBranch;
    }

    public String getPosition() {
        return position;
    }

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }
}