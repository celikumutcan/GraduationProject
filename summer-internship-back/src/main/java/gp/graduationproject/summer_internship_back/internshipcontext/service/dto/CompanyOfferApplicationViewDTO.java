package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

/**
 * DTO for showing student applications for a company offer.
 */
public class CompanyOfferApplicationViewDTO {

    private String fullName;
    private String userName;
    private String fileName;

    public CompanyOfferApplicationViewDTO(String fullName, String userName, String fileName) {
        this.fullName = fullName;
        this.userName = userName;
        this.fileName = fileName;
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
}