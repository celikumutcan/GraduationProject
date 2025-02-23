package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "approved_internships", schema = "public")
public class ApprovedInternship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentUserName;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private LocalDate internshipStartDate;

    @Column(nullable = false)
    private LocalDate internshipEndDate;

    @Column(nullable = false)
    private boolean assigned = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_instructor")
    private AcademicStaff assignedInstructor;

    // ✅ Getter ve Setter Metodları

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getInternshipStartDate() {
        return internshipStartDate;
    }

    public void setInternshipStartDate(LocalDate internshipStartDate) {
        this.internshipStartDate = internshipStartDate;
    }

    public LocalDate getInternshipEndDate() {
        return internshipEndDate;
    }

    public void setInternshipEndDate(LocalDate internshipEndDate) {
        this.internshipEndDate = internshipEndDate;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public AcademicStaff getAssignedInstructor() {
        return assignedInstructor;
    }

    public void setAssignedInstructor(AcademicStaff assignedInstructor) {
        this.assignedInstructor = assignedInstructor;
    }
}