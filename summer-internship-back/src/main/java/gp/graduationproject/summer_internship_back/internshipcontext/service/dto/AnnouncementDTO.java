package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

public class AnnouncementDTO {

    private String title;
    private String content;
    private String addedBy;
    private Instant datetime;

    public AnnouncementDTO(String title, String content, String addedBy, Instant datetime) {
        this.title = title;
        this.content = content;
        this.addedBy = addedBy;
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }
}


