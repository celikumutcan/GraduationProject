package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

public class EvaluateFormDTO {

    private Integer id;
    private Integer workingDay;
    private String performance;
    private String feedback;

    // Constructor
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