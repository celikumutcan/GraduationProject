package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

public class CompanyRegularApplicationViewDTO {
    private Long applicationId;
    private String position;
    private Instant applicationDate;
    private String fullName;
    private String userName;
    private String fileName;
    private String status;

    public CompanyRegularApplicationViewDTO(Long applicationId, String position, Instant applicationDate,
                                            String fullName, String userName, String fileName, String status) {
        this.applicationId = applicationId;
        this.position = position;
        this.applicationDate = applicationDate;
        this.fullName = fullName;
        this.userName = userName;
        this.fileName = fileName;
        this.status = status;
    }

    public Instant getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Instant applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
