package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * Entity representing a form record in the "forms" table.
 */
@Entity
@Table(name = "forms")
public class Form {

    /** Primary key of the form, generated using a sequence strategy. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forms_id_gen")
    @SequenceGenerator(name = "forms_id_gen", sequenceName = "forms_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    /** The timestamp when the form was created. */
    @NotNull
    @Column(name = "datetime", nullable = false)
    private Instant datetime;

    /** The file content stored as a String. */
    @Column(name = "file", nullable = false)
    private byte[] file;

    /** Additional content or description for the form. */
    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    /** The user (AcademicStaff) who added the form. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "add_user_name", nullable = false)
    private AcademicStaff addUserName;

    // Getters and Setters
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public Instant getDatetime()
    {
        return datetime;
    }
    public void setDatetime(Instant datetime)
    {
        this.datetime = datetime;
    }
    public byte[] getFile() {
        return file;
    }
    public void setFile(byte[] file) {
        this.file = file;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public AcademicStaff getAddUserName()
    {
        return addUserName;
    }
    public void setAddUserName(AcademicStaff addUserName)
    {
        this.addUserName = addUserName;
    }
}