package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "report_evaluation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"report_id", "item_name"})
})
public class ReportEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(nullable = true)
    private Double score;

    @Column(nullable = true)
    private String comment;

    @Column(nullable = true)
    private Double weight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}