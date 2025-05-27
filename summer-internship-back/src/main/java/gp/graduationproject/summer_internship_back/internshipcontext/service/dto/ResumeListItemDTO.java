package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

/**
 * Lightweight DTO for listing resumes without fetching full entity data.
 */
public class ResumeListItemDTO {
    private Integer id;
    private String fileName;
    private String fileType;
    private String userName;

    public ResumeListItemDTO(Integer id, String fileName, String fileType, String userName) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getUserName() {
        return userName;
    }
}