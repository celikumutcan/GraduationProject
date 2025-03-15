package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing internship offers in the database.
 */
@Repository
public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, Integer> {

    /**
     * Retrieves all internship offers with a specific status.
     *
     * @param status The status of the internship offers.
     * @return List of internship offers matching the given status.
     */
    List<InternshipOffer> findAllByStatus(String status);

    /**
     * Retrieves all internship offers associated with a specific company branch.
     *
     * @param companyBranch The company branch associated with the internship offers.
     * @return List of internship offers for the given company branch.
     */
    List<InternshipOffer> findAllByCompanyBranch(CompanyBranch companyBranch);

    /**
     * Retrieves all internship offers posted by a specific company using the company userName.
     *
     * @param userName The userName of the company.
     * @return List of internship offers from the specified company.
     */


    List<InternshipOffer> findByCompanyBranch_CompanyUserName_UserName(String userName);



    // 📌 Önerilen staj pozisyonları ile eşleşen InternshipOffer'ları getir
    @Query("SELECT io FROM InternshipOffer io WHERE LOWER(io.position) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<InternshipOffer> findByKeyword(@Param("keyword") String keyword);


    @Query("SELECT DISTINCT io.companyBranch FROM InternshipOffer io WHERE LOWER(io.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    List<CompanyBranch> findCompaniesByPosition(@Param("position") String position);

}