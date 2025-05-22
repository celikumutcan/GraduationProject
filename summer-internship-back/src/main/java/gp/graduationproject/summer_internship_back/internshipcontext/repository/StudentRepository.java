package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.StudentUsernameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Find a student by username.
     *
     * @param userName The username of the student.
     * @return Optional of Student.
     */
    Optional<Student> findByUserName(String userName);


    @Query("SELECT s.userName AS userName FROM Student s WHERE s.userName = :username")
    Optional<StudentUsernameDTO> findUsernameOnlyByUserName(@Param("username") String username);

}
