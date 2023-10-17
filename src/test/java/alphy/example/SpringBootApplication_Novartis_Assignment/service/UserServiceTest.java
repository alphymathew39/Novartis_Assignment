package alphy.example.SpringBootApplication_Novartis_Assignment.service;

import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import alphy.example.SpringBootApplication_Novartis_Assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterUserSuccess() {
        // Mock the behavior of the userRepository
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        User mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        // Mock the password encoder
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
        // Call the registerUser method
        ResponseEntity<String> response = userService.registerUser(mockUser);
        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody());
    }

    @Test
    void testRegisterUserConflict() {
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));
        User mockUser = new User();
        mockUser.setUsername("existingUser");
        ResponseEntity<String> response = userService.registerUser(mockUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User with the same username already exists.", response.getBody());
    }

    @Test
    void testAuthenticateUser() {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        User mockUser = new User();
        mockUser.setUsername("testUser");
        boolean result = userService.authenticateUser(mockUser, userDetails);
        assertTrue(result);
    }

    @Test
    void testAuthenticateUserFailure() {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        User mockUser = new User();
        mockUser.setUsername("differentUser");
        boolean result = userService.authenticateUser(mockUser, userDetails);
        assertFalse(result);
    }
}
