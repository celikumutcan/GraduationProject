package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "internship_offer")
@Getter
@Setter
public class InternshipOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer offerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    @NotNull
    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @NotNull
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "details", nullable = false, columnDefinition = "TEXT")
    private String details;

    @Column(name = "status", length = 50, nullable = false)
    private String status = "Open";

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}