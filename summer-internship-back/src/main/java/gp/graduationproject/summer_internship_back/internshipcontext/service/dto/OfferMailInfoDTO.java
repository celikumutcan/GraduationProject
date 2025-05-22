package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

public class OfferMailInfoDTO {
    private String studentEmail;
    private String studentUsername;
    private String companyEmail;
    private String branchName;
    private String position;
    private String department;

    // ➕ constructor
    public OfferMailInfoDTO(String studentEmail, String studentUsername,
                            String companyEmail, String branchName,
                            String position, String department) {
        this.studentEmail = studentEmail;
        this.studentUsername = studentUsername;
        this.companyEmail = companyEmail;
        this.branchName = branchName;
        this.position = position;
        this.department = department;
    }

    // ➕ Getters (setters gerekmez)
    public String getStudentEmail() { return studentEmail; }
    public String getStudentUsername() { return studentUsername; }
    public String getCompanyEmail() { return companyEmail; }
    public String getBranchName() { return branchName; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
}