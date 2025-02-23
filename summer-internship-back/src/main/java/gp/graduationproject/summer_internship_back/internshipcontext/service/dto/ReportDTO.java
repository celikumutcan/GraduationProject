package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;

/**
 * Data Transfer Object (DTO) for Report.
 */
public class ReportDTO {

    private Integer id;
    private String grade;
    private String feedback;

    /**
     * Constructs a ReportDTO from a Report entity.
     *
     * @param report the Report entity
     */
    public ReportDTO(Report report) {
        this.id = report.getId();
        this.grade = report.getGrade();
        this.feedback = report.getFeedback();
    }

    /**
     * Constructs a ReportDTO with specific values.
     *
     * @param id       the ID of the report
     * @param grade    the grade given in the report
     * @param feedback the feedback from the report
     */
    public ReportDTO(Integer id, String grade, String feedback) {
        this.id = id;
        this.grade = grade;
        this.feedback = feedback;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
}