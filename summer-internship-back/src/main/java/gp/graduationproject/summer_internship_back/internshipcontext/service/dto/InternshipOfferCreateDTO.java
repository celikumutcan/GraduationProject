package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.LocalDate;

/**
 * DTO for creating a new internship offer.
 * Sent by the company branch when posting a new offer.
 */
public class InternshipOfferCreateDTO {

    /**
     * ID of the company branch creating the offer.
     */
    private Integer companyBranchId;

    /**
     * Name of the internship position.
     */
    private String position;

    /**
     * Department related to the internship.
     */
    private String department;

    /**
     * Start date of the internship.
     */
    private LocalDate startDate;

    /**
     * End date of the internship.
     */
    private LocalDate endDate;

    /**
     * Detailed explanation about the internship.
     */
    private String details;

    /**
     * Optional short description for the internship.
     */
    private String description;

    private String companyUserName;


    // Getters and Setters
    public Integer getCompanyBranchId() {
        return companyBranchId;
    }

    public void setCompanyBranchId(Integer companyBranchId) {
        this.companyBranchId = companyBranchId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }
}