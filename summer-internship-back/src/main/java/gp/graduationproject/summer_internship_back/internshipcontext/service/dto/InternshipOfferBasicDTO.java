package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;

/**
 * DTO for holding minimal info about internship offer.
 */
public interface InternshipOfferBasicDTO {
    Integer getOfferId();
    String getPosition();
    CompanyBranch getCompanyBranch();
}