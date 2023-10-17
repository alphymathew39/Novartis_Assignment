package alphy.example.SpringBootApplication_Novartis_Assignment.repository;

import alphy.example.SpringBootApplication_Novartis_Assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Query method to find a user by username

    Optional<User> findByUsernameAndPassword(String username, String password);
}
