package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

public class CompanyDTO {
    private String fname;

    public CompanyDTO(String fname) {
        this.fname = fname;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
}
