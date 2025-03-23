package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "approved_trainee_information_form", schema = "public")
public class ApprovedTraineeInformationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approved_trainee_information_form_id_gen")
    @SequenceGenerator(name = "approved_trainee_information_form_id_gen", sequenceName = "approved_trainee_information_form_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "datetime", nullable = false)
    private LocalDate datetime;

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
    @Column(name = "supervisor_name",  length = 50)
    private String supervisorName;

    @Size(max = 50)
    @Column(name = "supervisor_surname",  length = 50)
    private String supervisorSurname;

    @NotNull
    @Column(name = "health_insurance", nullable = false)
    private Boolean healthInsurance = false;

    @NotNull
    @Column(name = "insurance_approval", nullable = false)
    private Boolean insuranceApproval = false;

    @Column(name = "insurance_approval_date")
    private LocalDate insuranceApprovalDate;

    @NotNull
    @Column(name = "status", nullable = false, length = 60)
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "fill_user_name", nullable = false)
    private Student fillUserName;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "coordinator_user_name", nullable = false)
    private AcademicStaff coordinatorUserName;

    @Size(max = 255)
    @Column(name = "evaluating_faculty_member")
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
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 50)
    @NotNull
    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "traineeInformationForm")
    private Set<EvaluateForm> evaluateForms = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "traineeInformationForm")
    private Set<Report> reports = new LinkedHashSet<>();

    // Getter ve Setter Metodları

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

    public Student getFillUserName() {
        return fillUserName;
    }

    public void setFillUserName(Student fillUserName) {
        this.fillUserName = fillUserName;
    }

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }

    public AcademicStaff getCoordinatorUserName() {
        return coordinatorUserName;
    }

    public void setCoordinatorUserName(AcademicStaff coordinatorUserName) {
        this.coordinatorUserName = coordinatorUserName;
    }

    public String getEvaluatingFacultyMember() {
        return evaluatingFacultyMember;
    }

    public void setEvaluatingFacultyMember(String evaluatingFacultyMember) {
        this.evaluatingFacultyMember = evaluatingFacultyMember;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Set<EvaluateForm> getEvaluateForms() {
        return evaluateForms;
    }

    public void setEvaluateForms(Set<EvaluateForm> evaluateForms) {
        this.evaluateForms = evaluateForms;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
}