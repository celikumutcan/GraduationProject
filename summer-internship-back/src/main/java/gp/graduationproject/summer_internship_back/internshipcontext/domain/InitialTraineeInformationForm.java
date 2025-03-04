package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "initial_trainee_information_form", schema = "public")
public class InitialTraineeInformationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "initial_trainee_information_form_id_gen")
    @SequenceGenerator(name = "initial_trainee_information_form_id_gen", sequenceName = "initial_trainee_information_form_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "datetime", nullable = false)
    private Instant datetime;

    @Size(max = 50)
    @NotNull
    @Column(name = "\"position\"", nullable = false, length = 50)
    private String position;

    @Size(max = 50)
    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Size(max = 20)
    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Size(max = 20)
    @NotNull
    @Column(name = "semester", nullable = false, length = 20)
    private String semester;

    @Size(max = 50)
    @Column(name = "supervisor_name", length = 50)
    private String supervisorName;

    @Size(max = 50)
    @Column(name = "supervisor_surname", length = 50)
    private String supervisorSurname;

    @NotNull
    @Column(name = "health_insurance", nullable = false)
    private Boolean healthInsurance = false;

    @Size(max = 60)
    @NotNull
    @Column(name = "status", nullable = false, length = 60)
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "fill_user_name", nullable = false)
    private Student fillUserName;

    @Size(max = 50)
    @Column(name = "company_user_name", length = 50)
    private String companyUserName;

    @Size(max = 255)
    @Column(name = "branch_name")
    private String branchName;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "coordinator_user_name", nullable = false) // Güncellendi
    private AcademicStaff coordinatorUserName;

    @Size(max = 255)
    @Column(name = "company_branch_address")
    private String companyBranchAddress;

    @Size(max = 20)
    @Column(name = "company_branch_phone", length = 20)
    private String companyBranchPhone;

    @Size(max = 100)
    @Column(name = "company_branch_email", length = 100)
    private String companyBranchEmail;

    @Size(max = 255)
    @Column(name = "evaluating_faculty_member") // Yeni Alan Eklendi
    private String evaluatingFacultyMember;

    @NotNull
    @Column(name = "internship_start_date", nullable = false)
    private LocalDate internshipStartDate;

    @NotNull
    @Column(name = "internship_end_date", nullable = false)
    private LocalDate internshipEndDate;

    @Size(max = 50)
    @NotNull
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Size(max = 50)
    @NotNull
    @Column(name = "city", nullable = true, length = 50)
    private String city;

    @Size(max = 50)
    @NotNull
    @Column(name = "district", nullable = false, length = 50)
    private String district;

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public LocalDate getInternshipEndDate() {
        return internshipEndDate;
    }
    public void setInternshipEndDate(LocalDate internshipEndDate) {
        this.internshipEndDate = internshipEndDate;
    }
    public LocalDate getInternshipStartDate() {
        return internshipStartDate;
    }
    public void setInternshipStartDate(LocalDate internshipStartDate) {
        this.internshipStartDate = internshipStartDate;
    }
    public String getCompanyBranchEmail() {
        return companyBranchEmail;
    }
    public void setCompanyBranchEmail(String companyBranchEmail) {
        this.companyBranchEmail = companyBranchEmail;
    }
    public String getCompanyBranchPhone() {
        return companyBranchPhone;
    }
    public void setCompanyBranchPhone(String companyBranchPhone) {
        this.companyBranchPhone = companyBranchPhone;
    }
    public String getCompanyBranchAddress() {
        return companyBranchAddress;
    }
    public void setCompanyBranchAddress(String companyBranchAddress) {
        this.companyBranchAddress = companyBranchAddress;
    }
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
    public gp.graduationproject.summer_internship_back.internshipcontext.domain.Student getFillUserName() {
        return fillUserName;
    }
    public void setFillUserName(gp.graduationproject.summer_internship_back.internshipcontext.domain.Student fillUserName) {
        this.fillUserName = fillUserName;
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
    public String getEvaluatingFacultyMember() {
        return evaluatingFacultyMember;
    }
    public void setEvaluatingFacultyMember(String evaluatingFacultyMember) {
        this.evaluatingFacultyMember = evaluatingFacultyMember;
    }
    public AcademicStaff getCoordinatorUserName() {
        return coordinatorUserName;
    }
    public void setCoordinatorUserName(AcademicStaff coordinatorUserName) {
        this.coordinatorUserName = coordinatorUserName;
    }
}