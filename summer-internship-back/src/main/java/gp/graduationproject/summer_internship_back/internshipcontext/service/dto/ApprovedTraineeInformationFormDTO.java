package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;
import java.util.List;

public class ApprovedTraineeInformationFormDTO {

    private Integer id;
    private String name;
    private String lastName;
    private String username;
    private Instant datetime;
    private String position;
    private String type;
    private String code;
    private String semester;
    private String supervisorName;
    private String supervisorSurname;
    private Boolean healthInsurance;
    private String status;
    private String companyUserName;
    private String branchName;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String evaluateUserName;
    private List<EvaluateFormDTO> evaluateForms;
    private List<ReportDTO> reports;

    public ApprovedTraineeInformationFormDTO(Integer id, String name , String lastName, String username, Instant datetime, String position, String type, String code,
                                             String semester, String supervisorName, String supervisorSurname,
                                             Boolean healthInsurance, String status, String companyUserName,
                                             String branchName, String companyAddress, String companyPhone,
                                             String companyEmail, String evaluateUserName,
                                             List<EvaluateFormDTO> evaluateForms, List<ReportDTO> reports) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.datetime = datetime;
        this.position = position;
        this.type = type;
        this.code = code;
        this.semester = semester;
        this.supervisorName = supervisorName;
        this.supervisorSurname = supervisorSurname;
        this.healthInsurance = healthInsurance;
        this.status = status;
        this.companyUserName = companyUserName;
        this.branchName = branchName;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.companyEmail = companyEmail;
        this.evaluateUserName = evaluateUserName;
        this.evaluateForms = evaluateForms;
        this.reports = reports;
    }

    // Getters and Setters for all fields
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getEvaluateUserName() {
        return evaluateUserName;
    }

    public void setEvaluateUserName(String evaluateUserName) {
        this.evaluateUserName = evaluateUserName;
    }

    public List<EvaluateFormDTO> getEvaluateForms() {
        return evaluateForms;
    }

    public void setEvaluateForms(List<EvaluateFormDTO> evaluateForms) {
        this.evaluateForms = evaluateForms;
    }

    public List<ReportDTO> getReports() {
        return reports;
    }

    public void setReports(List<ReportDTO> reports) {
        this.reports = reports;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

