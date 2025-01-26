package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "academic_staff", schema = "public")
public class AcademicStaff {
    @Id
    @Size(max = 50)
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @NotNull
    @Column(name = "iflag", nullable = false)
    private Boolean iflag = false;

    @NotNull
    @Column(name = "cflag", nullable = false)
    private Boolean cflag = false;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_name", nullable = false)
    private User users;

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIflag() {
        return iflag;
    }

    public void setIflag(Boolean iflag) {
        this.iflag = iflag;
    }

    public Boolean getCflag() {
        return cflag;
    }

    public void setCflag(Boolean cflag) {
        this.cflag = cflag;
    }

}