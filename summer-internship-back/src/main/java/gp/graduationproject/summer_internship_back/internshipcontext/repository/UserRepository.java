package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
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
}
