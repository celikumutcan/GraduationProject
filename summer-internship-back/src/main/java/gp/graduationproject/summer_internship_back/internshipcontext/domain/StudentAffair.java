package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "student_affairs", schema = "public")
public class StudentAffair {
    @Id
    @Size(max = 50)
    @SequenceGenerator(name = "student_affairs_id_gen", sequenceName = "report_id_seq", allocationSize = 1)
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_name", nullable = false)
    private User users;

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

}