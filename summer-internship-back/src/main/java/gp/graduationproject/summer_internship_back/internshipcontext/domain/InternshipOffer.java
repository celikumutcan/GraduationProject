package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Represents an internship offer created by a company branch.
 * Contains all necessary information for students to view and apply for internships.
 */
@Entity
@Table(name = "internship_offer")
public class InternshipOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer offerId;

    /**
     * The company branch that created this internship offer.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    /**
     * Name of the internship position.
     */
    @NotNull
    @Column(name = "position", nullable = false, length = 100)
    private String position;

    /**
     * Department of the internship.
     */
    @NotNull
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    /**
     * Start date of the internship.
     */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * End date of the internship.
     */
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Additional details provided by the company about this offer.
     */
    @NotNull
    @Column(name = "details", nullable = false, columnDefinition = "TEXT")
    private String details;

    /**
     * Status of the offer. Default is "Open".
     */
    @Column(name = "status", length = 50, nullable = false)
    private String status;

    /**
     * Timestamp of when the offer was created.
     */
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Short description to summarize the offer (optional).
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Default constructor setting initial status and creation time.
     */
    public InternshipOffer() {
        this.status = "Open";
        this.createdAt = Instant.now();
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @Column(name = "image_type")
    private String imageType;

    // Getters and Setters

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}