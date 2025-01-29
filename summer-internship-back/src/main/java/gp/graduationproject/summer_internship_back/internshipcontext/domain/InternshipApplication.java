package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity representing an internship application.
 */
@Entity
@Table(name = "Internship_Application")
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;  // Unique ID for the internship application

    @ManyToOne
    @JoinColumn(name = "student_user_name", nullable = false)
    private Student student;  // The student who applied for the internship

    @ManyToOne
    @JoinColumn(name = "company_branch_id")
    private CompanyBranch companyBranch;  // The company branch where the student applied

    @Column(name = "position", nullable = false, length = 50)
    private String position;  // The position the student applied for

    @Column(name = "application_date", updatable = false)
    private Instant applicationDate = Instant.now();  // The application submission date

    @Column(name = "status", length = 50)
    private String status = "Pending";  // Application status (Pending, Approved, Rejected)

    /**
     * Default constructor.
     */
    public InternshipApplication() {}

    /**
     * Constructor to create a new internship application.
     * @param student The student applying for the internship.
     * @param companyBranch The company branch where the internship is available.
     * @param position The position for which the student is applying.
     */
    public InternshipApplication(Student student, CompanyBranch companyBranch, String position) {
        this.student = student;
        this.companyBranch = companyBranch;
        this.position = position;
        this.applicationDate = Instant.now();
        this.status = "Pending";
    }


    // Getters and Setters

    public Long getApplicationId() {
        return applicationId;
    }

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }


    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Instant getApplicationDate() {
        return applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}