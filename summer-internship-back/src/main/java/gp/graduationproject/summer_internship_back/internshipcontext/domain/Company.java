package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "company", schema = "public")
public class Company {
    @Id
    @Size(max = 50)
    @SequenceGenerator(name = "company_id_gen", sequenceName = "company_branch_branch_id_seq", allocationSize = 1)
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_name", nullable = false)
    private User users;


    @OneToMany(mappedBy = "companyUserName")
    private Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch> companyBranches = new LinkedHashSet<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch> getCompanyBranches() {
        return companyBranches;
    }

    public void setCompanyBranches(Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch> companyBranches) {
        this.companyBranches = companyBranches;
    }

}