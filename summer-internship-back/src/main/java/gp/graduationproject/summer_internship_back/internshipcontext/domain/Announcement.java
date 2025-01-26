package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "announcement", schema = "public")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_id_gen")
    @SequenceGenerator(name = "announcement_id_gen", sequenceName = "announcement_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "datetime", nullable = false)
    private Instant datetime;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "add_user_name", nullable = false)
    private AcademicStaff addUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AcademicStaff getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(AcademicStaff addUserName) {
        this.addUserName = addUserName;
    }

}