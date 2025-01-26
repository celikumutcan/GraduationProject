package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); // Retrieves all reports
    }

    public User getUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }


}

