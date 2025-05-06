package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;

/**
 * Entity class representing an evaluation form for a student's internship.
 */
@Entity
@Table(name = "Evaluate_Form")
public class EvaluateForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_information_form_id", nullable = false)
    private ApprovedTraineeInformationForm traineeInformationForm;

    /**
     * Student's attendance evaluation (Excellent, Good, Satisfactory, Unsatisfactory).
     */
    @Column(name = "attendance", nullable = false, length = 50)
    private String attendance;

    /**
     * Student's diligence and enthusiasm evaluation (Excellent, Good, Satisfactory, Unsatisfactory).
     */
    @Column(name = "diligence_and_enthusiasm", nullable = false, length = 50)
    private String diligenceAndEnthusiasm;

    /**
     * Student's contribution to work environment evaluation (Excellent, Good, Satisfactory, Unsatisfactory).
     */
    @Column(name = "contribution_to_work_environment", nullable = false, length = 50)
    private String contributionToWorkEnvironment;

    /**
     * Student's overall performance evaluation (Excellent, Good, Satisfactory, Unsatisfactory).
     */
    @Column(name = "overall_performance", nullable = false, length = 50)
    private String overallPerformance;

    /**
     * Additional comments about the student.
     */
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ApprovedTraineeInformationForm getTraineeInformationForm() {
        return traineeInformationForm;
    }

    public void setTraineeInformationForm(ApprovedTraineeInformationForm traineeInformationForm) {
        this.traineeInformationForm = traineeInformationForm;
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