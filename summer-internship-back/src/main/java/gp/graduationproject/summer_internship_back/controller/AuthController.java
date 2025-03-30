package gp.graduationproject.summer_internship_back.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.service.UserService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.LoginRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserService userService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody LoginRequestDTO loginRequest) {
        boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (isValid) {
            User user = userService.getUserByUserName(loginRequest.getUsername());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "user_id", user.getUserName(),
                    "user_name", user.getUserName(),
                    "first_name", user.getFirstName(),
                    "last_name", user.getLastName(),
                    "email", user.getEmail(),
                    "user_type", user.getUserType()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid credentials"));
        }
    }
}