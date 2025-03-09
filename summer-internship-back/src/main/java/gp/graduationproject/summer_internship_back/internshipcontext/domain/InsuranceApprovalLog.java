package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing the insurance approval log.
 * This table stores records of insurance approvals made by Student Affairs.
 */
@Entity
@Table(name = "Insurance_Approval_Log")
public class InsuranceApprovalLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "approved_trainee_id", nullable = false)
    private Integer approvedTraineeId;

    @Column(name = "student_user_name", nullable = false)
    private String studentUserName;

    @Column(name = "company_branch_id", nullable = false)
    private Integer companyBranchId;

    @Column(name = "internship_start_date", nullable = false)
    private LocalDate internshipStartDate;

    @Column(name = "internship_end_date", nullable = false)
    private LocalDate internshipEndDate;

    @Column(name = "branch_address", nullable = false)
    private String branchAddress;

    @Column(name = "health_insurance", nullable = false)
    private Boolean healthInsurance;

    @Column(name = "approval_date", nullable = false)
    private LocalDateTime approvalDate;

    @Column(name = "approved_by", nullable = false)
    private String approvedBy;

    /**
     * Default constructor.
     */
    public InsuranceApprovalLog() {
    }

    /**
     * Parameterized constructor to create a log entry.
     *
     * @param approvedTraineeId   ID of the approved trainee.
     * @param studentUserName     Username of the student.
     * @param companyBranchId     ID of the company branch.
     * @param internshipStartDate Internship start date.
     * @param internshipEndDate   Internship end date.
     * @param branchAddress       Address of the company branch.
     * @param healthInsurance     Health insurance status.
     * @param approvalDate        Date and time of approval.
     * @param approvedBy          Username of the approved.
     */
    public InsuranceApprovalLog(Integer approvedTraineeId, String studentUserName, Integer companyBranchId,
                                LocalDate internshipStartDate, LocalDate internshipEndDate, String branchAddress,
                                Boolean healthInsurance, LocalDateTime approvalDate, String approvedBy) {
        this.approvedTraineeId = approvedTraineeId;
        this.studentUserName = studentUserName;
        this.companyBranchId = companyBranchId;
        this.internshipStartDate = internshipStartDate;
        this.internshipEndDate = internshipEndDate;
        this.branchAddress = branchAddress;
        this.healthInsurance = healthInsurance;
        this.approvalDate = approvalDate;
        this.approvedBy = approvedBy;
    }

    // Getters and Setters
    public Integer getLogId() { return logId; }
    public Integer getApprovedTraineeId() { return approvedTraineeId; }
    public String getStudentUserName() { return studentUserName; }
    public Integer getCompanyBranchId() { return companyBranchId; }
    public LocalDate getInternshipStartDate() { return internshipStartDate; }
    public LocalDate getInternshipEndDate() { return internshipEndDate; }
    public String getBranchAddress() { return branchAddress; }
    public Boolean getHealthInsurance() { return healthInsurance; }
    public LocalDateTime getApprovalDate() { return approvalDate; }
    public String getApprovedBy() { return approvedBy; }
    public void setLogId(Integer logId) { this.logId = logId; }
    public void setApprovedTraineeId(Integer approvedTraineeId) { this.approvedTraineeId = approvedTraineeId; }
    public void setStudentUserName(String studentUserName) { this.studentUserName = studentUserName; }
    public void setCompanyBranchId(Integer companyBranchId) { this.companyBranchId = companyBranchId; }
    public void setInternshipStartDate(LocalDate internshipStartDate) { this.internshipStartDate = internshipStartDate; }
    public void setInternshipEndDate(LocalDate internshipEndDate) { this.internshipEndDate = internshipEndDate; }
    public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }
    public void setHealthInsurance(Boolean healthInsurance) { this.healthInsurance = healthInsurance; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    @Override
    public String toString() {
        return "InsuranceApprovalLog{" +
                "logId=" + logId +
                ", approvedTraineeId=" + approvedTraineeId +
                ", studentUserName='" + studentUserName + '\'' +
                ", companyBranchId=" + companyBranchId +
                ", internshipStartDate=" + internshipStartDate +
                ", internshipEndDate=" + internshipEndDate +
                ", branchAddress='" + branchAddress + '\'' +
                ", healthInsurance=" + healthInsurance +
                ", approvalDate=" + approvalDate +
                ", approvedBy='" + approvedBy + '\'' +
                '}';
    }
}