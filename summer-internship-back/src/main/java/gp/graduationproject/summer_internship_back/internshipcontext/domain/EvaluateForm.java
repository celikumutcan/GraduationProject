package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Evaluate_Form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}