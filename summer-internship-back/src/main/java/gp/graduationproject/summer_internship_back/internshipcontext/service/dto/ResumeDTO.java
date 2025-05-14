package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

public class ResumeDTO {
    private Integer id;
    private String userName;
    private String fileName;
    private String fileType;

    public ResumeDTO() {
    }

    public ResumeDTO(Integer id, String userName, String fileName, String fileType)
    {
        this.id = id;
        this.userName = userName;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
