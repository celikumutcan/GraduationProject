package gp.graduationproject.summer_internship_back.internshipcontext.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * Announcement entity represents a database table storing announcements.
 * This entity is mapped to the "announcement" table in the "public" schema.
 */
@Entity                                                     // Marks this class as a JPA entity (maps to a database table)
@Table(name = "announcement", schema = "public")           // Maps this entity to the "announcement" table in the "public" schema
public class Announcement {

    @Id                                                     // Defines this field as the primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_id_gen")
    // Specifies that the primary key is generated using a sequence
    @SequenceGenerator(name = "announcement_id_gen", sequenceName = "announcement_id_seq", allocationSize = 1)
    // Defines the sequence generator: uses "announcement_id_seq" sequence and increments by 1
    @Column(name = "id", nullable = false)
    private Integer id;                                      // Unique identifier for each announcement

    @NotNull                                                // Ensures this field cannot be null
    @Column(name = "datetime", nullable = false)
    private Instant datetime;                               // Stores the timestamp of when the announcement was created

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;                                  // Stores the content of the announcement

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)    // Defines a Many-to-One relationship with AcademicStaff
    @OnDelete(action = OnDeleteAction.CASCADE)              // If the related AcademicStaff is deleted, this announcement will also be deleted
    @JoinColumn(name = "add_user_name", nullable = false)   // Foreign key column linking to the "add_user_name" column in AcademicStaff
    private AcademicStaff addUserName;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath; // Dosyanın saklandığı yerin yolu
// The academic staff who created the announcement

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