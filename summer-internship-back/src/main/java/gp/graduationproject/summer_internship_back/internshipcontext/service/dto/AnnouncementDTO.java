package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

import java.time.Instant;

public class AnnouncementDTO {

    private final String title;
    private final String content;
    private final String addedBy;
    private final Instant datetime;
    private final String filePath;

    public AnnouncementDTO(String title, String content, String addedBy, Instant datetime, String filePath) {
        this.title = title;
        this.content = content;
        this.addedBy = addedBy;
        this.datetime = datetime;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileUrl() {
        return (filePath != null && !filePath.isEmpty())
                ? "http://localhost:8080/api/files/download/" + filePath
                : null;
    }
}
