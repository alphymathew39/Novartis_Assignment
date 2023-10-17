package alphy.example.SpringBootApplication_Novartis_Assignment.service;

import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import alphy.example.SpringBootApplication_Novartis_Assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testLoadUserByUsername() {
        // Mock a User entity and an optional
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        Optional<User> userOptional = Optional.of(user);
        // Mock the repository's behavior
        when(userRepository.findByUsername("testUser")).thenReturn(userOptional);
        // Call the loadUserByUsername method
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
        // Verify the result
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        // Mock an empty optional to simulate user not found
        Optional<User> userOptional = Optional.empty();
        // Mock the repository's behavior
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(userOptional);
        // Call the loadUserByUsername method and expect a UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonExistentUser");
        });
    }
}
