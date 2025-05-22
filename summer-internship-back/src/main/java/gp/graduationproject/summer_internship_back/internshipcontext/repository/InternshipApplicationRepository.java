package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipApplication;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.CompanyOfferApplicationViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for InternshipApplication.
 * Provides methods to interact with the database.
 */
@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {

    List<InternshipApplication> findByInternshipOffer(InternshipOffer internshipOffer);
    List<InternshipApplication> findByStudent(Student student);
    List<InternshipApplication> findByCompanyBranch(CompanyBranch companyBranch);


    @Query("""
SELECT ia FROM InternshipApplication ia
JOIN FETCH ia.companyBranch
WHERE ia.student.userName = :username
""")
    List<InternshipApplication> findAllByStudentUserNameWithBranch(@Param("username") String username);

    boolean existsByStudentUserName_UserNameAndInternshipOffer_OfferId(String userName, Integer offerId);

    @Query("""
SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.CompanyOfferApplicationViewDTO(
    CONCAT(u.firstName, ' ', u.lastName),
    s.userName,
    r.fileName
)
FROM InternshipApplication ia
JOIN ia.student s
JOIN s.users u
LEFT JOIN s.resumes r
WHERE ia.internshipOffer.offerId = :offerId
""")
    List<CompanyOfferApplicationViewDTO> getAllApplicantsWithCV(Integer offerId);

}