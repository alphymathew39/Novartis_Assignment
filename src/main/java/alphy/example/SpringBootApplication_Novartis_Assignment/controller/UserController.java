package alphy.example.SpringBootApplication_Novartis_Assignment.controller;

import alphy.example.SpringBootApplication_Novartis_Assignment.config.JwtConfig;
import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import alphy.example.SpringBootApplication_Novartis_Assignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        logger.info("Registering a new user: " + user.getUsername());

        ResponseEntity<String> response = userService.registerUser(user);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            logger.info("User registration successful.");
        } else {
            logger.error("User registration failed.");
        }
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        logger.info("User login attempt: " + user.getUsername());

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        if (userDetails == null) {
            logger.warn("Authentication failed. User not found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }

        if (userService.authenticateUser(user, userDetails)) {
            logger.info("User authenticated successfully: " + user.getUsername());
            final String jwt = jwtConfig.generateToken(userDetails); // Use the JwtConfig to generate a token
            return ResponseEntity.ok(jwt);
        } else {
            logger.warn("Authentication failed. Incorrect credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }
    }
}
