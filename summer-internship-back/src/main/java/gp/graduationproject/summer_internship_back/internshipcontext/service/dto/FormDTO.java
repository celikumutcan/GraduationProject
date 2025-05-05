package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

/**
 * Data Transfer Object for Form entity.
 */
public class FormDTO {

    private Integer id;
    private Instant datetime;
    private String addedBy;
    private String content;

    public FormDTO() {
    }

    public FormDTO(Integer id, Instant datetime, String addedBy, String content) {
        this.id = id;
        this.datetime = datetime;
        this.addedBy = addedBy;
        this.content = content;
    }

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

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}