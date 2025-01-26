package gp.graduationproject.summer_internship_back.dbfunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbFunctions {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addReport(int traineeInformationFormId, String grade, String feedback) {
        String query = "INSERT INTO Report (trainee_information_form_id, grade, feedback) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, traineeInformationFormId, grade, feedback);
        System.out.println("Report Added Successfully");
    }

    public void updateReport(int reportId, String grade, String feedback) {
        String query = "UPDATE Report SET grade = ?, feedback = ? WHERE id = ?";
        jdbcTemplate.update(query, grade, feedback, reportId);
        System.out.println("Report Updated Successfully");
    }

    public void deleteReport(int reportId) {
        String query = "DELETE FROM Report WHERE id = ?";
        jdbcTemplate.update(query, reportId);
        System.out.println("Report Deleted Successfully");
    }

    public void addResume(String filePath, String description){
        String query = "INSERT INTO Resume (filePath, description) VALUES (?, ?)";
        jdbcTemplate.update(query, filePath, description);
        System.out.println("Resume Added Successfully");
    }

    public void updateResume(int resumeId, String filePath, String description) {
        String query = "UPDATE Resume SET reportId = ?, grade = ?, feedback = ? WHERE id = ?";
        jdbcTemplate.update(query, filePath, description, resumeId);
        System.out.println("Resume Updated Successfully");
    }

    public void deleteResume(int resumeId) {
        String query = "DELETE FROM Resume WHERE id = ?";
        jdbcTemplate.update(query, resumeId);
        System.out.println("Resume Deleted Successfully");
    }

    public void addCompany(String userName){
        String query = "INSERT INTO Company (userName, phone) VALUES (?, ?)";
        jdbcTemplate.update(query, userName);
        System.out.println("Company Added Successfully");
    }

    public void addCompanyBranch(String companyUserName, String branchName, String address, String phone){
        String query = "INSERT INTO Company_Branch (companyUserName, branchName, address, phone) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, companyUserName, branchName, address, phone);
        System.out.println("Company Branch Added Successfully");
    }

    public void addAnnouncement(String content, String addUserName){
        String query = "INSERT INTO Announcement (content, addUserName) VALUES (?, ?)";
        jdbcTemplate.update(query, content, addUserName);
        System.out.println("Announcement Added Successfully");
    }

    public void updateAnnouncement( int announcementId, String title, String content) {
        String query = "UPDATE Announcement SET announcementId = ?, title = ?, content = ? WHERE id = ?";
        jdbcTemplate.update(query,  title, content, announcementId);
        System.out.println("Announcement Updated Successfully");
    }

    public void deleteAnnouncement(int announcementId) {
        String query = "DELETE FROM Announcement WHERE id = ?";
        jdbcTemplate.update(query, announcementId);
        System.out.println("Announcement Deleted Successfully");
    }

    public void addForm(String formName , String formDescription){
        String query = "INSERT INTO Form (formName, formDescription) VALUES (?, ?)";
        jdbcTemplate.update(query, formName, formDescription);
        System.out.println("Form Added Successfully");
    }

    public void updateForm(int formId, String formName, String formDescription){
        String query = "INSERT INTO Form (formId, formName, formDescription) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, formName, formDescription,formId);
        System.out.println("Form Updated Successfully");
    }

    public void deleteForm(int formId) {
        String query = "DELETE FROM Form WHERE id = ?";
        jdbcTemplate.update(query, formId);
        System.out.println("Form Deleted Successfully");
    }

    public void addEvaluateForm(int traineeInformationFormId, int workingDay, String performance, String feedback){
        String query = "INSERT INTO Evaluate_Form (traineeInformationFormId, workingDay, performance, feedback) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, traineeInformationFormId, workingDay, performance, feedback);
        System.out.println("Evaluation Form Added Successfully");
    }

    public void updateEvaluateForm(int evaluateFormId, int workingDay, String performance, String feedback){
        String query = "INSERT INTO Evaluate_Form (evaluateFormId, workingDay, performance, feedback) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, workingDay, performance, feedback, evaluateFormId);
        System.out.println("Evaluation Form Updated Successfully");
    }

    public void deleteEvaluateForm(int evaluateFormId) {
        String query = "DELETE FROM Evaluate_Form WHERE id = ?";
        jdbcTemplate.update(query, evaluateFormId);
        System.out.println("Form Deleted Successfully");
    }

    public void addApprovedTraineeInformationForm(String position, String type, String code,
                                                  String semester, String supervisorName, String supervisorSurname, boolean healthInsurance,
                                                  String fillUserName, int branchId, String companyEmail, String evaluateUserName){
        String query = "INSERT INTO Approved_Trainee_Information_Form "
                + "(datetime, position, type, code, semester, supervisor_name, supervisor_surname, "
                + "health_insurance, status, fill_user_name, branch_id, company_email, evaluate_user_name) "
                + "VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?, 'Approved', ?, ?, ?, ?);";
        jdbcTemplate.update(query, position, type, code, semester, supervisorName, supervisorSurname, healthInsurance, fillUserName, branchId, companyEmail, evaluateUserName);
        System.out.println("Approved Trainee Information Form Added Successfully");
    }

    public void addInitialTraineeInformationForm( String position, String type, String code,
                                                  String semester, String supervisorName, String supervisorSurname, boolean healthInsurance,
                                                  String status, String fillUserName, String companyUserName, String branchName,
                                                  String companyAddress, String companyPhone, String companyEmail, String evaluateUserName){
        String query = "INSERT INTO Initial_Trainee_Information_Form "
                + "(datetime, position, type, code, semester, supervisor_name, supervisor_surname, "
                + "health_insurance, status, fill_user_name, company_user_name, branch_name, "
                + "company_address, company_phone, company_email, evaluate_user_name) "
                + "VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(query, position, type, code, semester, supervisorName, supervisorSurname, healthInsurance, status, fillUserName, companyUserName,branchName,companyAddress,companyPhone, companyEmail, evaluateUserName);
        System.out.println("Initial Trainee Information Form Added Successfully");
    }

    public void updateTraineeInformationForm(int formId, String position, String type, String code,
                                   String semester, String supervisorName, String supervisorSurname,
                                   boolean healthInsurance, String status, String address, String companyPhone){
        String query = "UPDATE Trainee_Information_Form SET position = ?, type = ?, code = ?, semester = ?, " +
                "supervisor_name = ?, supervisor_surname = ?, health_insurance = ?, status = ?, address = ?, " +
                "company_phone = ? WHERE id = ?;";
        jdbcTemplate.update(query, position, type, code, semester, supervisorName, supervisorSurname, healthInsurance, status, address, companyPhone, formId);
        System.out.println("Trainee Information Form Updated Successfully");
    }

    public void deleteTraineeInformationForm(int formId) {
        String query = "DELETE FROM Trainee_Information_Form WHERE id = ?";
        jdbcTemplate.update(query, formId);
        System.out.println("Trainee Information Deleted Successfully");
    }
}
