package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.EvaluateForm;

/**
 * Data Transfer Object (DTO) for EvaluateForm.
 */
public class EvaluateFormDTO {

    private Integer id;
    private Integer workingDay;
    private String performance;
    private String feedback;

    /**
     * Constructs an EvaluateFormDTO from an EvaluateForm entity.
     *
     * @param evaluateForm the EvaluateForm entity
     */
    public EvaluateFormDTO(EvaluateForm evaluateForm) {
        this.id = evaluateForm.getId();
        this.workingDay = evaluateForm.getWorkingDay();
        this.performance = evaluateForm.getPerformance();
        this.feedback = evaluateForm.getFeedback();
    }

    /**
     * Constructs an EvaluateFormDTO with specific values.
     *
     * @param id          the ID of the evaluation form
     * @param workingDay  the number of working days
     * @param performance the performance rating
     * @param feedback    the feedback from the evaluation
     */
    public EvaluateFormDTO(Integer id, Integer workingDay, String performance, String feedback) {
        this.id = id;
        this.workingDay = workingDay;
        this.performance = performance;
        this.feedback = feedback;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getWorkingDay() {
        return workingDay;
    }
    public void setWorkingDay(Integer workingDay) {
        this.workingDay = workingDay;
    }
    public String getPerformance() {
        return performance;
    }
    public void setPerformance(String performance) {
        this.performance = performance;
    }
    public String getFeedback() {
        return feedback;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}