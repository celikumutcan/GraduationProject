package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User{

    @Id
    @Column(name = "user_name", nullable = false, unique = true, length = 50)
    private String userName;

    @Column(name = "fname", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lname", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "user_type", nullable = false, length = 20)
    private String userType;

    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    private AcademicStaff academicStaff;

    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    private Company company;

    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    private StudentAffair studentAffair;

    @OneToOne(mappedBy = "branchUserName", fetch = FetchType.LAZY)
    private CompanyBranch companyBranch;

    public StudentAffair getStudentAffair() {
        return studentAffair;
    }

    public void setStudentAffair(StudentAffair studentAffair) {
        this.studentAffair = studentAffair;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AcademicStaff getAcademicStaff() {
        return academicStaff;
    }

    public void setAcademicStaff(AcademicStaff academicStaff) {
        this.academicStaff = academicStaff;
    }

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public String getFullName(){
        return firstName + " " + lastName;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
