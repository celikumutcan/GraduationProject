package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Represents an internship offer provided by a company branch.
 * Includes details such as position, department, start and end dates, and status.
 */
@Entity
@Table(name = "internship_offer")
public class InternshipOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer offerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    @NotNull
    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @NotNull
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "details", nullable = false, columnDefinition = "TEXT")
    private String details;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Default constructor required by JPA.
     */
    public InternshipOffer() {
        this.status = "Open";
        this.createdAt = Instant.now();
    }

    // Getter and Setter Methods
    public Integer getOfferId()
    {
        return offerId;
    }
    public void setOfferId(Integer offerId)
    {
        this.offerId = offerId;
    }
    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }
    public void setCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
    }
    public String getPosition()
    {
        return position;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }
    public String getDepartment()
    {
        return department;
    }
    public void setDepartment(String department)
    {
        this.department = department;
    }
    public LocalDate getStartDate()
    {
        return startDate;
    }
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }
    public LocalDate getEndDate()
    {
        return endDate;
    }
    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }
    public String getDetails()
    {
        return details;
    }
    public void setDetails(String details)
    {
        this.details = details;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public Instant getCreatedAt()
    {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt = createdAt;
    }
}