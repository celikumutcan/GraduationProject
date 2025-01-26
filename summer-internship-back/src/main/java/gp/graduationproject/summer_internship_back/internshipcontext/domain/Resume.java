package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entity representing a Resume.
 */
@Entity
@Table(name = "resume")
public class Resume {

    /** Auto-generated ID. */
    @Id
    @ColumnDefault("nextval('resume_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    /** Associated student (foreign key). */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_name", nullable = false)
    private Student userName;

    /** File content of the resume. */
    @NotNull
    @Column(name = "file", nullable = false, length = Integer.MAX_VALUE)
    private String file;

    // Getter and Setter for id
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }

    // Getter and Setter for userName
    public Student getUserName()
    {
        return userName;
    }
    public void setUserName(Student userName)
    {
        this.userName = userName;
    }

    // Getter and Setter for file
    public String getFile()
    {
        return file;
    }
    public void setFile(String file)
    {
        this.file = file;
    }
}