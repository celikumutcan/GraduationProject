package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Report entity.
 */
public class ReportDTO {

    private Integer id;
    private Integer traineeInformationFormId;
    private String userName;
    private String grade;
    private String feedback;
    private String status;

    @JsonIgnore
    private MultipartFile file;

    private String fileBase64;

    private LocalDateTime createdAt;

    /**
     * Default constructor for deserialization.
     */
    public ReportDTO() {
    }

    /**
     * Constructor that maps Report entity to DTO.
     * Only metadata is included; file content is excluded.
     *
     * @param report the Report entity
     */
    public ReportDTO(Report report) {
        this.id = report.getId();
        this.traineeInformationFormId = report.getTraineeInformationForm().getId();
        this.grade = report.getGrade();
        this.feedback = report.getFeedback();
        this.status = report.getStatus();
        this.createdAt = report.getCreatedAt();
        // fileBase64 is intentionally not set to avoid performance issue
    }

    /**
     * Full constructor with all fields.
     */
    public ReportDTO(Integer id, Integer traineeInformationFormId, String userName, String grade, String feedback, String status, MultipartFile file, LocalDateTime createdAt) {
        this.id = id;
        this.traineeInformationFormId = traineeInformationFormId;
        this.userName = userName;
        this.grade = grade;
        this.feedback = feedback;
        this.status = status;
        this.file = file;
        this.createdAt = createdAt;
    }

    /**
     * Constructor for partial use cases such as filtering by instructor.
     */
    public ReportDTO(Integer id, String grade, String feedback, String status, LocalDateTime createdAt) {
        this.id = id;
        this.grade = grade;
        this.feedback = feedback;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTraineeInformationFormId() {
        return traineeInformationFormId;
    }

    public void setTraineeInformationFormId(Integer traineeInformationFormId) {
        this.traineeInformationFormId = traineeInformationFormId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}