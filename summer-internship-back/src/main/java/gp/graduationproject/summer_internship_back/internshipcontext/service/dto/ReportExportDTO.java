package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

/**
 * This DTO is used for exporting student report data to Excel.
 * It contains only the necessary fields.
 */
public record ReportExportDTO(
        String firstName,
        String lastName,
        String userName,
        String code,
        String grade
) {}