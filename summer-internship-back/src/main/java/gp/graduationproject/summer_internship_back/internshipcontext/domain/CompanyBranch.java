package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "company_branch")
public class CompanyBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_branch_id_gen")
    @SequenceGenerator(name = "company_branch_id_gen", sequenceName = "company_branch_branch_id_seq", allocationSize = 1)
    @Column(name = "branch_id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_user_name", nullable = false)
    private User branchUserName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_user_name", nullable = false)
    private Company companyUserName;

    @Size(max = 255)
    @NotNull
    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @NotNull
    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    private String address;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 100)
    @NotNull
    @Column(name = "branch_email", nullable = false, length = 100)
    private String branchEmail;

    @OneToMany(mappedBy = "companyBranch")
    private Set<ApprovedTraineeInformationForm> approvedTraineeInformationForms = new LinkedHashSet<>();

    @Size(max = 50)
    @NotNull
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Size(max = 50)
    @NotNull
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 50)
    @NotNull
    @Column(name = "district", nullable = false, length = 50)
    private String district;


    // Getter and Setter Methods
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public User getBranchUserName()
    {
        return branchUserName;
    }
    public void setBranchUserName(User branchUserName)
    {
        this.branchUserName = branchUserName;
    }
    public Company getCompanyUserName()
    {
        return companyUserName;
    }
    public void setCompanyUserName(Company companyUserName)
    {
        this.companyUserName = companyUserName;
    }
    public String getBranchName()
    {
        return branchName;
    }
    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getBranchEmail()
    {
        return branchEmail;
    }
    public void setBranchEmail(String branchEmail)
    {
        this.branchEmail = branchEmail;
    }
    public Set<ApprovedTraineeInformationForm> getApprovedTraineeInformationForms()
    {
        return approvedTraineeInformationForms;
    }
    public void setApprovedTraineeInformationForms(Set<ApprovedTraineeInformationForm> approvedTraineeInformationForms)
    {
        this.approvedTraineeInformationForms = approvedTraineeInformationForms;
    }
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public String getDistrict()
    {
        return district;
    }
    public void setDistrict(String district)
    {
        this.district = district;
    }
    public String getCountry()
    {
        return country;
    }
    public void setCountry(String country)
    {
        this.country = country;
    }
    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

}