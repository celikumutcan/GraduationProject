package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InternshipOfferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing internship offers.
 */
@Service
public class InternshipOfferService {

    private final InternshipOfferRepository internshipOfferRepository;
    private final CompanyBranchRepository companyBranchRepository;

    /**
     * Constructor-based dependency injection.
     */
    public InternshipOfferService(InternshipOfferRepository internshipOfferRepository, CompanyBranchRepository companyBranchRepository) {
        this.internshipOfferRepository = internshipOfferRepository;
        this.companyBranchRepository = companyBranchRepository;
    }

    /**
     * Creates a new internship offer.
     */
    public void createInternshipOffer(Integer branchId, String position, String department, String details, String startDate, String endDate) {
        CompanyBranch branch = companyBranchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Company branch not found"));

        InternshipOffer offer = new InternshipOffer();
        offer.setCompanyBranch(branch);
        offer.setPosition(position);
        offer.setDepartment(department);
        offer.setDetails(details);
        offer.setStartDate(LocalDate.parse(startDate));
        offer.setEndDate(LocalDate.parse(endDate));
        offer.setStatus("OPEN"); // Default status for a new offer

        internshipOfferRepository.save(offer);
    }

    /**
     * Retrieves all open internship offers.
     */
    public List<InternshipOffer> getAllOpenInternshipOffers() {
        return internshipOfferRepository.findAllByStatus("OPEN");
    }

    /**
     * Retrieves all internship offers from a specific company.
     */
    public List<InternshipOffer> getCompanyInternshipOffers(Integer companyId) {
        return internshipOfferRepository.findByCompanyBranch_Company_Id(companyId);
    }

    /**
     * Updates an existing internship offer.
     */
    public void updateInternshipOffer(Integer offerId, String position, String department, String details, String startDate, String endDate, String status) {
        InternshipOffer offer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found"));

        offer.setPosition(position);
        offer.setDepartment(department);
        offer.setDetails(details);
        offer.setStartDate(LocalDate.parse(startDate));
        offer.setEndDate(LocalDate.parse(endDate));
        offer.setStatus(status);

        internshipOfferRepository.save(offer);
    }

    /**
     * Deletes an internship offer.
     */
    public void deleteInternshipOffer(Integer offerId) {
        InternshipOffer offer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found"));

        internshipOfferRepository.delete(offer);
    }
}