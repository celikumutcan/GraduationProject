package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApprovedTraineeInformationFormCompanyDTO {
    private Integer id;
    private String studentFirstName;
    private String studentLastName;
    private String studentUserName;
    private LocalDate datetime;
    private String position;
    private String type;
    private String supervisorName;
    private String supervisorSurname;
    private Boolean healthInsurance;
    private Boolean insuranceApproval;
    private LocalDate insuranceApprovalDate;
    private String status;
    private String companyUserName;
    private String branchName;
    private LocalDate internshipStartDate;
    private LocalDate internshipEndDate;

    public ApprovedTraineeInformationFormCompanyDTO(
            Integer id,
            String studentFirstName,
            String studentLastName,
            String studentUserName,
            LocalDate datetime,
            String position,
            String type,
            String supervisorName,
            String supervisorSurname,
            Boolean healthInsurance,
            Boolean insuranceApproval,
            LocalDate insuranceApprovalDate,
            String status,
            String companyUserName,
            String branchName,
            LocalDate internshipStartDate,
            LocalDate internshipEndDate
    ) {
        this.id = id;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentUserName = studentUserName;
        this.datetime = datetime;
        this.position = position;
        this.type = type;
        this.supervisorName = supervisorName;
        this.supervisorSurname = supervisorSurname;
        this.healthInsurance = healthInsurance;
        this.insuranceApproval = insuranceApproval;
        this.insuranceApprovalDate = insuranceApprovalDate;
        this.status = status;
        this.companyUserName = companyUserName;
        this.branchName = branchName;
        this.internshipStartDate = internshipStartDate;
        this.internshipEndDate = internshipEndDate;
    }



    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDate datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorSurname() {
        return supervisorSurname;
    }

    public void setSupervisorSurname(String supervisorSurname) {
        this.supervisorSurname = supervisorSurname;
    }

    public Boolean getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(Boolean healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public Boolean getInsuranceApproval() {
        return insuranceApproval;
    }

    public void setInsuranceApproval(Boolean insuranceApproval) {
        this.insuranceApproval = insuranceApproval;
    }

    public LocalDate getInsuranceApprovalDate() {
        return insuranceApprovalDate;
    }

    public void setInsuranceApprovalDate(LocalDate insuranceApprovalDate) {
        this.insuranceApprovalDate = insuranceApprovalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public LocalDate getInternshipStartDate() {
        return internshipStartDate;
    }

    public void setInternshipStartDate(LocalDate internshipStartDate) {
        this.internshipStartDate = internshipStartDate;
    }

    public LocalDate getInternshipEndDate() {
        return internshipEndDate;
    }

    public void setInternshipEndDate(LocalDate internshipEndDate) {
        this.internshipEndDate = internshipEndDate;
    }
}
