package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import java.time.LocalDate;

public class InternshipOfferDTO {
    private Integer offerId;
    private String position;
    private String department;
    private LocalDate startDate;
    private LocalDate endDate;
    private String details;
    private byte[] imageData;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    private String imageType;
    // Constructor
    public InternshipOfferDTO(InternshipOffer offer) {
        this.offerId = offer.getOfferId();
        this.position = offer.getPosition();
        this.department = offer.getDepartment();
        this.startDate = offer.getStartDate();
        this.endDate = offer.getEndDate();
        this.details = offer.getDetails();
        this.imageData = offer.getImageData();
        this.imageType = offer.getImageType();

    }



    // Getters
    public Integer getOfferId() {
        return offerId;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDetails() {
        return details;
    }
}