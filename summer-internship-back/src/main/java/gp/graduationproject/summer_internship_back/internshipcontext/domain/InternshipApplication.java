package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents an internship application submitted by a student.
 * Each application is linked to a student, a company branch, and an internship offer.
 */
@Entity
@Table(name = "Internship_Application")
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "student_user_name", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "company_branch_id")
    private CompanyBranch companyBranch;

    @Column(name = "position", nullable = false, length = 50)
    private String position;

    @Column(name = "application_date", updatable = false)
    private Instant applicationDate;

    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne
    @JoinColumn(name = "internship_offer_id", nullable = false)
    private InternshipOffer internshipOffer;

    /**
     * Default constructor required by JPA.
     */
    public InternshipApplication() {
        this.status = "Pending";
        this.applicationDate = Instant.now();
    }

    /**
     * Constructor to create a new internship application.
     * @param student The student applying for the internship.
     * @param internshipOffer The internship offer the student is applying for.
     */
    public InternshipApplication(Student student, InternshipOffer internshipOffer) {
        this.student = student;
        this.internshipOffer = internshipOffer;
        this.applicationDate = Instant.now();
        this.status = "Pending";
    }

    // Getter and Setter Methods
    public Long getApplicationId()
    {
        return applicationId;
    }
    public void setApplicationId(Long applicationId)
    {
        this.applicationId = applicationId;
    }
    public Student getStudent()
    {
        return student;
    }
    public void setStudent(Student student)
    {
        this.student = student;
    }
    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }
    public void setCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
    }
    public String getPosition()
    {
        return position;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }
    public Instant getApplicationDate()
    {
        return applicationDate;
    }
    public void setApplicationDate(Instant applicationDate)
    {
        this.applicationDate = applicationDate;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public InternshipOffer getInternshipOffer()
    {
        return internshipOffer;
    }
    public void setInternshipOffer(InternshipOffer internshipOffer)
    {
        this.internshipOffer = internshipOffer;
    }
}