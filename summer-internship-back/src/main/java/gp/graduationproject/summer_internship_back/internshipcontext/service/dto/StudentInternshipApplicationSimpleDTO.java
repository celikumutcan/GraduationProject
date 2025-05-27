package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

/**
 * Lightweight DTO for internship applications of a student.
 */
public class StudentInternshipApplicationSimpleDTO {

    private Long applicationId;
    private String studentUsername;
    private String branchName;
    private String position;
    private Instant applicationDate;
    private String status;
    private Integer offerId;

    public StudentInternshipApplicationSimpleDTO(Long applicationId, String studentUsername, String branchName,
                                                 String position, Instant applicationDate, String status,
                                                 Integer offerId) {
        this.applicationId = applicationId;
        this.studentUsername = studentUsername;
        this.branchName = branchName;
        this.position = position;
        this.applicationDate = applicationDate;
        this.status = status;
        this.offerId = offerId;
    }


    // Getters
    public Long getApplicationId() {
        return applicationId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getPosition() {
        return position;
    }

    public Instant getApplicationDate() {
        return applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public Integer getOfferId() {
        return offerId;
    }
}