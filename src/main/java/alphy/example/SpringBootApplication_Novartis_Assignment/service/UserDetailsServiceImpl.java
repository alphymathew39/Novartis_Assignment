package alphy.example.SpringBootApplication_Novartis_Assignment.service;

import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import alphy.example.SpringBootApplication_Novartis_Assignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        logger.info("Loading user by username: " + username);
        if (!userOptional.isPresent()) {
            logger.warn("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new HashSet<>());
    }
}
