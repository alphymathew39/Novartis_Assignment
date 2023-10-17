package alphy.example.SpringBootApplication_Novartis_Assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy // Enable AspectJ AOP support
@ComponentScan("alphy.example.SpringBootApplication_Novartis_Assignment")
public class SpringBootApplicationNovartisAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationNovartisAssignmentApplication.class, args);
    }

}
