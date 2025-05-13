package gp.graduationproject.summer_internship_back.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.UserService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.LoginRequestDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.UserLoginResponseDTO;

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

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Verifies the user credentials and returns basic user information if login is successful.
     *
     * @param loginRequest contains username and password
     * @return 200 OK with user info if valid, 401 UNAUTHORIZED if invalid
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody LoginRequestDTO loginRequest) {
        boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (isValid) {
            UserLoginResponseDTO userDTO = userService.getUserLoginDTO(loginRequest.getUsername());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "user_id", userDTO.getUserName(),
                    "user_name", userDTO.getUserName(),
                    "first_name", userDTO.getFirstName(),
                    "last_name", userDTO.getLastName(),
                    "email", userDTO.getEmail(),
                    "user_type", userDTO.getUserType()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid credentials"));
        }
    }
}