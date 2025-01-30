package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
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
    @NotNull
    @Column(name = "supervisor_name", nullable = false, length = 50)
    private String supervisorName;

    @Size(max = 50)
    @NotNull
    @Column(name = "supervisor_surname", nullable = false, length = 50)
    private String supervisorSurname;

    @NotNull
    @Column(name = "health_insurance", nullable = false)
    private Boolean healthInsurance = false;

    @NotNull
    @Column(name = "status", nullable = false, length = 60)
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "fill_user_name", nullable = false)
    private Student fillUserName;

    @Size(max = 100)
    @Column(name = "company_email", length = 100)
    private String companyEmail;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "evaluate_user_name", nullable = false)
    private AcademicStaff evaluateUserName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "traineeInformationForm")
    private Set<EvaluateForm> evaluateForms = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "traineeInformationForm")
    private Set<Report> reports = new LinkedHashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    @NotNull
    @Column(name = "insurance_approval", nullable = false)
    private Boolean insuranceApproval = false;

    @Column(name = "insurance_approval_date")
    private Instant insuranceApprovalDate;

    @NotNull
    @Column(name = "internship_start_date", nullable = false)
    private Instant internshipStartDate;

    @NotNull
    @Column(name = "internship_end_date", nullable = false)
    private Instant internshipEndDate;

    public Instant getInternshipStartDate() {
        return internshipStartDate;
    }

    public void setInternshipStartDate(Instant internshipStartDate) {
        this.internshipStartDate = internshipStartDate;
    }

    public Instant getInternshipEndDate() {
        return internshipEndDate;
    }

    public void setInternshipEndDate(Instant internshipEndDate) {
        this.internshipEndDate = internshipEndDate;
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

    public Student getFillUserName() {
        return fillUserName;
    }

    public void setFillUserName(Student fillUserName) {
        this.fillUserName = fillUserName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public AcademicStaff getEvaluateUserName() {
        return evaluateUserName;
    }

    public void setEvaluateUserName(AcademicStaff evaluateUserName) {
        this.evaluateUserName = evaluateUserName;
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

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }

    public Boolean getInsuranceApproval() {
        return insuranceApproval;
    }

    public void setInsuranceApproval(Boolean insuranceApproval) {
        this.insuranceApproval = insuranceApproval;
    }

    public Instant getInsuranceApprovalDate() {
        return insuranceApprovalDate;
    }

    public void setInsuranceApprovalDate(Instant insuranceApprovalDate) {
        this.insuranceApprovalDate = insuranceApprovalDate;
    }


}