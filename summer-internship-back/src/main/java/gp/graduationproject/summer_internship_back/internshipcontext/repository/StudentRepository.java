package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Find a student by username.
     *
     * @param userName The username of the student.
     * @return Optional of Student.
     */


    Optional<Student> findByUserName(String userName);
}
