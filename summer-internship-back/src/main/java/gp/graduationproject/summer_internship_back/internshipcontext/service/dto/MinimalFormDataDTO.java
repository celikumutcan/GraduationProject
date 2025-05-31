package gp.graduationproject.summer_internship_back.internshipcontext.service.dto;

/**
 * A projection interface to retrieve only minimal information
 * from the ApprovedTraineeInformationForm entity.
 *
 * This is used for performance optimization when full entity loading is not needed.
 */
public interface MinimalFormDataDTO {

    /**
     * Gets the status of the trainee information form.
     *
     * @return the form status (e.g., "Approved")
     */
    String getStatus();

    /**
     * Gets the username of the evaluating faculty member.
     *
     * @return the instructor's username
     */
    String getEvaluatingFacultyMember();

    /**
     * Gets the username of the student who filled the form.
     *
     * @return the student's username
     */
    String getFillUserName();
}
