package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

/**
 * DTO for transferring internship application data.
 */
public class InternshipApplicationDTO {

    private Long  id;
    private String studentUsername;
    private String branchName;
    private String position;
    private Instant applicationDate;
    private String status;

    /**
     * Constructor for InternshipApplicationDTO.
     * @param id The ID of the application.
     * @param studentUsername The username of the student who applied.
     * @param branchName The name of the company branch.
     * @param position The position applied for.
     * @param applicationDate The date the application was submitted.
     * @param status The status of the application (Pending, Approved, Rejected).
     */
    public InternshipApplicationDTO(Long  id, String studentUsername, String branchName, String position,
                                    Instant applicationDate, String status) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.branchName = branchName;
        this.position = position;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Instant getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Instant applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}