package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

/**
 * DTO for showing student applications for a company offer.
 */
public class CompanyOfferApplicationViewDTO {

    private Long applicationId;
    private String fullName;
    private String userName;
    private String fileName;
    private String status; // ✅ status alanı eklendi

    public CompanyOfferApplicationViewDTO(Long applicationId, String fullName, String userName, String fileName, String status) {
        this.applicationId = applicationId;
        this.fullName = fullName;
        this.userName = userName;
        this.fileName = fileName;
        this.status = status;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStatus() {
        return status;
    }
}