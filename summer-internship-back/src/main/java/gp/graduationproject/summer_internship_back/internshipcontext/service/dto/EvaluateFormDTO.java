package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;

/**
 * Data Transfer Object (DTO) for EvaluateForm.
 */
public class EvaluateFormDTO {

    private Integer id;
    private String attendance;
    private String diligenceAndEnthusiasm;
    private String contributionToWorkEnvironment;
    private String overallPerformance;
    private String comments;

    /**
     * Constructs an EvaluateFormDTO from an EvaluateForm entity.
     *
     * @param evaluateForm the EvaluateForm entity
     */
    public EvaluateFormDTO(EvaluateForm evaluateForm) {
        this.id = evaluateForm.getId();
        this.attendance = evaluateForm.getAttendance();
        this.diligenceAndEnthusiasm = evaluateForm.getDiligenceAndEnthusiasm();
        this.contributionToWorkEnvironment = evaluateForm.getContributionToWorkEnvironment();
        this.overallPerformance = evaluateForm.getOverallPerformance();
        this.comments = evaluateForm.getComments();
    }

    /**
     * Constructs an EvaluateFormDTO with specific values.
     *
     * @param id ID of the evaluation form
     * @param attendance Attendance evaluation
     * @param diligenceAndEnthusiasm Diligence and enthusiasm evaluation
     * @param contributionToWorkEnvironment Contribution to work environment evaluation
     * @param overallPerformance Overall performance evaluation
     * @param comments Additional comments
     */
    public EvaluateFormDTO(Integer id, String attendance, String diligenceAndEnthusiasm,
                           String contributionToWorkEnvironment, String overallPerformance, String comments) {
        this.id = id;
        this.attendance = attendance;
        this.diligenceAndEnthusiasm = diligenceAndEnthusiasm;
        this.contributionToWorkEnvironment = contributionToWorkEnvironment;
        this.overallPerformance = overallPerformance;
        this.comments = comments;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAttendance() {
        return attendance;
    }
    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
    public String getDiligenceAndEnthusiasm() {
        return diligenceAndEnthusiasm;
    }
    public void setDiligenceAndEnthusiasm(String diligenceAndEnthusiasm) {
        this.diligenceAndEnthusiasm = diligenceAndEnthusiasm;
    }
    public String getContributionToWorkEnvironment() {
        return contributionToWorkEnvironment;
    }
    public void setContributionToWorkEnvironment(String contributionToWorkEnvironment) {
        this.contributionToWorkEnvironment = contributionToWorkEnvironment;
    }
    public String getOverallPerformance() {
        return overallPerformance;
    }
    public void setOverallPerformance(String overallPerformance) {
        this.overallPerformance = overallPerformance;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
}