package alphy.example.SpringBootApplication_Novartis_Assignment.service;

import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import alphy.example.SpringBootApplication_Novartis_Assignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {

            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new HashSet<>());
    }

    public ResponseEntity<String> registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with the same username already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>()); // Set roles as needed
        userRepository.save(user);
        logger.info("Registering user: " + user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    public boolean authenticateUser(User user, UserDetails userDetails) {
        return userDetails.getUsername().equals(user.getUsername());
    }

}

