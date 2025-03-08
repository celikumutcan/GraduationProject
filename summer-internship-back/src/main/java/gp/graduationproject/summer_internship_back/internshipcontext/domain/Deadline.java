package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "deadline")
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "internship_deadline", nullable = true)
    private LocalDate internshipDeadline;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "report_deadline", nullable = true) // ✅ Burada nullable = true
    private LocalDate reportDeadline;

    // Boş constructor şart
    public Deadline() {
    }

    public Deadline(LocalDate internshipDeadline) {
        this.internshipDeadline = internshipDeadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInternshipDeadline() {
        return internshipDeadline;
    }

    public void setInternshipDeadline(LocalDate internshipDeadline) {
        this.internshipDeadline = internshipDeadline;
    }

    public LocalDate getReportDeadline() {
        return reportDeadline;
    }

    public void setReportDeadline(LocalDate reportDeadline) {
        this.reportDeadline = reportDeadline;
    }
}
