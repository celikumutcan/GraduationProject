package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;

/**
 * Data Transfer Object (DTO) for Report.
 */
public class ReportDTO {

    private Integer id;
    private Integer traineeInformationFormId;
    private String userName;
    private String grade;
    private String feedback;
    private String status;

    /**
     * Default constructor required for JSON deserialization.
     */
    public ReportDTO() {
    }

    /**
     * Constructs a ReportDTO from a Report entity.
     *
     * @param report the Report entity
     */
    public ReportDTO(Report report) {
        this.id = report.getId();
        this.traineeInformationFormId = report.getTraineeInformationForm().getId();
        this.grade = report.getGrade();
        this.feedback = report.getFeedback();
        this.status = report.getStatus();
    }

    /**
     * Constructs a ReportDTO with specific values.
     *
     * @param id                      the ID of the report
     * @param traineeInformationFormId the ID of the related trainee information form
     * @param userName                 the username of the student submitting the report
     * @param grade                    the grade given in the report
     * @param feedback                 the feedback from the report
     * @param status                   the status of the report
     */
    public ReportDTO(Integer id, Integer traineeInformationFormId, String userName, String grade, String feedback, String status) {
        this.id = id;
        this.traineeInformationFormId = traineeInformationFormId;
        this.userName = userName;
        this.grade = grade;
        this.feedback = feedback;
        this.status = status;
    }

    /**
     * Constructs a ReportDTO without trainee information form ID and username.
     *
     * @param id       the ID of the report
     * @param grade    the grade given in the report
     * @param feedback the feedback from the report
     * @param status   the status of the report
     */
    public ReportDTO(Integer id, String grade, String feedback, String status) {
        this.id = id;
        this.grade = grade;
        this.feedback = feedback;
        this.status = status;
    }

    // Getters and Setters
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
}