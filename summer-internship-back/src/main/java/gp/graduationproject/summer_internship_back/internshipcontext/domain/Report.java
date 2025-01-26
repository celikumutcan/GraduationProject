package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "report", schema = "public")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_gen")
    @SequenceGenerator(name = "report_id_gen", sequenceName = "report_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "trainee_information_form_id", nullable = false)
    private gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm traineeInformationForm;

    @Size(max = 10)
    @NotNull
    @Column(name = "grade", nullable = false, length = 10)
    private String grade;

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