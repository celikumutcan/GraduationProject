package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InitialTraineeInformationFormRepository extends JpaRepository<InitialTraineeInformationForm, Integer> {
    List<InitialTraineeInformationForm> findAllByFillUserName_UserName(String userName);
    Optional<InitialTraineeInformationForm> findByFillUserNameAndInternshipEndDateAndPosition(
            Student fillUserName, LocalDate internshipEndDate, String position);
}
