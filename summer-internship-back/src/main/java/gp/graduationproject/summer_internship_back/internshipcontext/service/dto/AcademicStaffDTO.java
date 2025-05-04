package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

/**
 * This class is a Data Transfer Object (DTO) for AcademicStaff.
 * It contains only username, first name, and last name fields.
 */
public class AcademicStaffDTO {

    private String userName;
    private String firstName;
    private String lastName;

    public AcademicStaffDTO() {
        // Default constructor
    }

    public AcademicStaffDTO(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}