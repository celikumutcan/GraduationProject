package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
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

    public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    public @NotNull User getBranchUserName() {
        return branchUserName;
    }

    public void setBranchUserName(@NotNull User branchUserName) {
        this.branchUserName = branchUserName;
    }

    public @NotNull Company getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(@NotNull Company companyUserName) {
        this.companyUserName = companyUserName;
    }

    public @Size(max = 255) @NotNull String getBranchName() {
        return branchName;
    }

    public void setBranchName(@Size(max = 255) @NotNull String branchName) {
        this.branchName = branchName;
    }

    public @NotNull String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    public @Size(max = 100) @NotNull String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(@Size(max = 100) @NotNull String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public Set<ApprovedTraineeInformationForm> getApprovedTraineeInformationForms() {
        return approvedTraineeInformationForms;
    }

    public void setApprovedTraineeInformationForms(Set<ApprovedTraineeInformationForm> approvedTraineeInformationForms) {
        this.approvedTraineeInformationForms = approvedTraineeInformationForms;
    }

    public @Size(max = 15) String getPhone() {
        return phone;
    }

    public void setPhone(@Size(max = 15) String phone) {
        this.phone = phone;
    }

    public @Size(max = 50) @NotNull String getDistrict() {
        return district;
    }

    public void setDistrict(@Size(max = 50) @NotNull String district) {
        this.district = district;
    }

    public @Size(max = 50) @NotNull String getCountry() {
        return country;
    }

    public void setCountry(@Size(max = 50) @NotNull String country) {
        this.country = country;
    }

    public @Size(max = 50) @NotNull String getCity() {
        return city;
    }

    public void setCity(@Size(max = 50) @NotNull String city) {
        this.city = city;
    }
}