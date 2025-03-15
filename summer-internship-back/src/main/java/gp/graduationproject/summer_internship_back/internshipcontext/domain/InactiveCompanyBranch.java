package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents an inactive company branch.
 * This entity stores inactive branches with a reason and timestamp.
 */
@Entity
@Table(name = "Inactive_Company_Branch")
public class InactiveCompanyBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer branchId;

    @Column(nullable = false)
    private String inactivationReason;

    @Column(nullable = false)
    private LocalDateTime inactivatedAt = LocalDateTime.now();

    // Constructor
    public InactiveCompanyBranch() {}

    public InactiveCompanyBranch(Integer branchId, String inactivationReason) {
        this.branchId = branchId;
        this.inactivationReason = inactivationReason;
        this.inactivatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getInactivationReason() {
        return inactivationReason;
    }

    public void setInactivationReason(String inactivationReason) {
        this.inactivationReason = inactivationReason;
    }

    public LocalDateTime getInactivatedAt() {
        return inactivatedAt;
    }

    public void setInactivatedAt(LocalDateTime inactivatedAt) {
        this.inactivatedAt = inactivatedAt;
    }
}