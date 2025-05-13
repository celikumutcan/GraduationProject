package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.StudentAffair;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.UserLoginResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, String> {
    User findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    List<User> findAllByUserType(@Param("userType") String userType);

    @Query("SELECT u.email FROM User u WHERE u.userType = :userType")
    List<String> findAllEmailsByUserType(@Param("userType") String userType);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.userType = :userType")
    Optional<User> findByEmailAndUserType(@Param("email") String email, @Param("userType") String userType);

    @Query("SELECT sa FROM StudentAffair sa")
    List<StudentAffair> findAllStudentAffairs();

    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.UserLoginResponseDTO(u.userName, u.firstName, u.lastName, u.email, u.userType) FROM User u WHERE u.userName = :username")
    UserLoginResponseDTO findLoginDTOByUserName(@Param("username") String username);

    @Query("SELECT u.password FROM User u WHERE u.userName = :username")
    String findPasswordByUserName(@Param("username") String username);
}
