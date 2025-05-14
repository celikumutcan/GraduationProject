package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "student", schema = "public")
public class Student {
    @Id
    @Size(max = 50)
    @SequenceGenerator(name = "student_id_gen", sequenceName = "report_id_seq", allocationSize = 1)
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_name", nullable = false)
    private User users;

    @OneToMany(mappedBy = "fillUserName")
    private Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm> approvedTraineeInformationForms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fillUserName")
    private Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm> initialTraineeInformationForms = new LinkedHashSet<>();

    public String getUserName() {
        return this.userName;

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

    public Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm> getApprovedTraineeInformationForms() {
        return approvedTraineeInformationForms;
    }

    public void setApprovedTraineeInformationForms(Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm> approvedTraineeInformationForms) {
        this.approvedTraineeInformationForms = approvedTraineeInformationForms;
    }

    public Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm> getInitialTraineeInformationForms() {
        return initialTraineeInformationForms;
    }

    public void setInitialTraineeInformationForms(Set<gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm> initialTraineeInformationForms) {
        this.initialTraineeInformationForms = initialTraineeInformationForms;
    }

}