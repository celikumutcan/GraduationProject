package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

public class CompanyBranchDTO {
    private String branch_name;

    public CompanyBranchDTO(String branch_name) {
        this.branch_name = branch_name;
    }
    public String getBranch_name() {
        return branch_name;
    }
    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
}
