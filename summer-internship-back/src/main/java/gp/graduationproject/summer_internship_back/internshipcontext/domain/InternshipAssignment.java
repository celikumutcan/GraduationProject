package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;

import java.time.Instant;
/*
@Entity
@Table(name = "internship_assignments", schema = "public")
public class InternshipAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "internship_id", nullable = false)
    private ApprovedInternship internship;

    @ManyToOne
    @JoinColumn(name = "academic_staff_id", nullable = false)
    private AcademicStaff instructor;  // 📌 Eğitmen ataması

    @Column(nullable = false)
    private Boolean isReviewed = false; // 📌 Eğitmen stajı değerlendirdi mi?

    @Column(nullable = false)
    private Instant assignedDate; // 📌 Atama tarihi

    public void setInternship(ApprovedInternship internship) {
        this.internship = internship;
    }
    public ApprovedInternship getInternship() {
        return internship;
    }

    public void setInstructor(AcademicStaff assignedInstructor) {
        this.instructor = assignedInstructor;
    }
    public AcademicStaff getInstructor() {
        return instructor;
    }

    public void setAssignedDate(Instant now) {
        this.assignedDate = now;
    }
    public Instant getAssignedDate() {
        return assignedDate;
    }


    // Getters & Setters
}
*/