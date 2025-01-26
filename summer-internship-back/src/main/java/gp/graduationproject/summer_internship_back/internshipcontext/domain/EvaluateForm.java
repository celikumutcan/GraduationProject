package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "evaluate_form", schema = "public")
public class EvaluateForm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evaluate_form_id_gen")
    @SequenceGenerator(name = "evaluate_form_id_gen", sequenceName = "evaluate_form_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "trainee_information_form_id", nullable = false)
    private gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm traineeInformationForm;

    @NotNull
    @Column(name = "working_day", nullable = false)
    private Integer workingDay;

    @Size(max = 50)
    @NotNull
    @Column(name = "performance", nullable = false, length = 50)
    private String performance;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm getTraineeInformationForm() {
        return traineeInformationForm;
    }

    public void setTraineeInformationForm(gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm traineeInformationForm) {
        this.traineeInformationForm = traineeInformationForm;
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