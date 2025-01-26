package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

public class ReportDTO {

    private Integer id;
    private String grade;
    private String feedback;

    // Constructor
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

