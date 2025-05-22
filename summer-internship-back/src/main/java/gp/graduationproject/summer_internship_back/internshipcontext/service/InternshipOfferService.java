package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InternshipOfferRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferCreateDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing internship offers.
 */
@Service
public class InternshipOfferService {

    private final InternshipOfferRepository internshipOfferRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    /**
     * Constructor-based dependency injection.
     */
    @Autowired
    public InternshipOfferService(InternshipOfferRepository internshipOfferRepository,
                                  CompanyBranchRepository companyBranchRepository,
                                  UserRepository userRepository,
                                  EmailService emailService) {
        this.internshipOfferRepository = internshipOfferRepository;
        this.companyBranchRepository = companyBranchRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /**
     * Creates a new internship offer and sends notification to all students.
     *
     * @param dto the internship offer info from company
     */
    public void createInternshipOffer(InternshipOfferCreateDTO dto) {
        if (dto.getCompanyUserName() == null || dto.getCompanyUserName().isBlank()) {
            throw new RuntimeException("Company username is required.");
        }

        CompanyBranch branch = companyBranchRepository.findByBranchUserName_UserName(dto.getCompanyUserName())
                .orElseThrow(() -> new RuntimeException("Company Branch not found with username: " + dto.getCompanyUserName()));

        InternshipOffer offer = new InternshipOffer();
        offer.setCompanyBranch(branch);
        offer.setPosition(dto.getPosition());
        offer.setDepartment(dto.getDepartment());
        offer.setDetails(dto.getDetails());
        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        offer.setDescription(dto.getDescription());
        offer.setStatus("OPEN");

        internshipOfferRepository.save(offer);

        List<String> studentEmails = userRepository.findAllStudentEmails();
        emailService.sendNewOfferNotificationToAllStudents(studentEmails, offer);
    }


    /**
     * Retrieves all open internship offers and returns them as DTOs.
     *
     * @return List of open internship offers in DTO format.
     */
    public List<InternshipOfferListDTO> getAllOpenInternshipOffersAsDTO() {
        return internshipOfferRepository.findAllOpenOffersAsDTO();
    }

    /**
     * Retrieves all internship offers created by a specific company user.
     *
     * @param userName The username of the company user.
     * @return List of internship offers.
     */
    public List<InternshipOffer> getCompanyInternshipOffers(String userName) {
        return internshipOfferRepository.findByCompanyBranch_CompanyUserName_UserName(userName);
    }

    /**
     * Updates an existing internship offer.
     *
     * @param offerId   ID of the offer to update.
     * @param position  New position title.
     * @param department New department.
     * @param details   Updated details.
     * @param startDate Updated start date.
     * @param endDate   Updated end date.
     * @param status    Updated status.
     */
    public void updateInternshipOffer(Integer offerId, String position, String department,
                                      String details, String startDate, String endDate, String status) {
        InternshipOffer offer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found"));

        offer.setPosition(position);
        offer.setDepartment(department);
        offer.setDetails(details);
        offer.setStartDate(java.time.LocalDate.parse(startDate));
        offer.setEndDate(java.time.LocalDate.parse(endDate));
        offer.setStatus(status);

        internshipOfferRepository.save(offer);
    }

    /**
     * Deletes an internship offer by ID.
     *
     * @param offerId ID of the offer to delete.
     */
    public void deleteInternshipOffer(Integer offerId) {
        InternshipOffer offer = internshipOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Internship offer not found"));

        internshipOfferRepository.delete(offer);
    }
}