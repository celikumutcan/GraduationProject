package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity representing an internship application.
 */
@Entity
@Table(name = "Internship_Application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private Instant applicationDate = Instant.now();

    @Column(name = "status", length = 50)
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "internship_offer_id", nullable = false)
    private InternshipOffer internshipOffer;

    /**
     * Constructor to create a new internship application.
     * @param student The student applying for the internship.
     * @param internshipOffer The internship offer the student is applying for.
     */
    public InternshipApplication(Student student, InternshipOffer internshipOffer)
    {
        this.student = student;
        this.internshipOffer = internshipOffer;
        this.applicationDate = Instant.now();
        this.status = "Pending";
    }
}