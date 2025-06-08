package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferBasicDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing internship offers in the database.
 */
@Repository
public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, Integer> {

    List<InternshipOffer> findByCompanyBranch_BranchUserName_UserName(String username);

    @Query("""
SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferListDTO(
    io.offerId,
    io.position,
    io.department,
    io.startDate,
    io.endDate,
    cb.branchName,
    io.details,
    io.description,
    cast(case when io.imageData is not null then true else false end as boolean)
                          )
FROM InternshipOffer io
JOIN io.companyBranch cb
WHERE io.status = 'OPEN'
""")
    List<InternshipOfferListDTO> findAllOpenOffersAsDTO();

    @Query("""
SELECT io.offerId AS offerId, io.position AS position, io.companyBranch AS companyBranch
FROM InternshipOffer io
WHERE io.offerId = :offerId
""")
    Optional<InternshipOfferBasicDTO> findBasicInfoById(@Param("offerId") Integer offerId);
}
