package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

public class BrowseInternshipApplicationDTO {
    private Integer branchId;
    private String position;
    public BrowseInternshipApplicationDTO( String position, Integer branchId) {
        this.branchId = branchId;
        this.position = position;

    }

    public Integer getBranchId() {
        return branchId;
    }

    public String getPosition() {
        return position;
    }
}
