package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Evaluate_Form")
public class EvaluateForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_information_form_id", nullable = false)
    private ApprovedTraineeInformationForm traineeInformationForm;

    @Column(name = "working_day", nullable = false)
    private Integer workingDay;

    @Column(name = "performance", nullable = false, length = 50)
    private String performance;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public ApprovedTraineeInformationForm getTraineeInformationForm()
    {
        return traineeInformationForm;
    }
    public void setTraineeInformationForm(ApprovedTraineeInformationForm traineeInformationForm)
    {
        this.traineeInformationForm = traineeInformationForm;
    }
    public Integer getWorkingDay()
    {
        return workingDay;
    }
    public void setWorkingDay(Integer workingDay)
    {
        this.workingDay = workingDay;
    }
    public String getPerformance()
    {
        return performance;
    }
    public void setPerformance(String performance)
    {
        this.performance = performance;
    }
    public String getFeedback()
    {
        return feedback;
    }
    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }
}
