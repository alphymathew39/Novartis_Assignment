package alphy.example.SpringBootApplication_Novartis_Assignment.controller;

import alphy.example.SpringBootApplication_Novartis_Assignment.config.JwtConfig;
import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import alphy.example.SpringBootApplication_Novartis_Assignment.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterUserSuccess() {
        // Mocking the UserService behavior
        when(userService.registerUser(any(User.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully"));
        // Create a mock User
        User mockUser = new User();
        // Test the registerUser endpoint
        ResponseEntity<String> response = userController.registerUser(mockUser);
        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void testRegisterUserFailure() {
        // Mocking the UserService behavior
        when(userService.registerUser(any(User.class))).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed"));
        User mockUser = new User();
        ResponseEntity<String> response = userController.registerUser(mockUser);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("User registration failed", response.getBody());
    }

    @Test
    void testLoginUserSuccess() {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(userService.authenticateUser(any(User.class), any(UserDetails.class))).thenReturn(true);
        when(jwtConfig.generateToken(userDetails)).thenReturn("fake.jwt.token");
        User mockUser = new User();
        mockUser.setUsername("testUser");
        ResponseEntity<String> response = userController.loginUser(mockUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fake.jwt.token", response.getBody());
    }

    @Test
    void testLoginUserNotFound() {
        when(userService.loadUserByUsername("nonExistentUser")).thenReturn(null);
        User mockUser = new User();
        mockUser.setUsername("nonExistentUser");
        ResponseEntity<String> response = userController.loginUser(mockUser);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed.", response.getBody());
    }

    @Test
    void testLoginUserAuthenticationFailed() {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(userService.authenticateUser(any(User.class), any(UserDetails.class))).thenReturn(false);
        User mockUser = new User();
        mockUser.setUsername("testUser");
        ResponseEntity<String> response = userController.loginUser(mockUser);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed.", response.getBody());
    }
}
