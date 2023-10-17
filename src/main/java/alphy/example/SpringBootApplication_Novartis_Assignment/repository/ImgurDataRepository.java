package alphy.example.SpringBootApplication_Novartis_Assignment.repository;

import alphy.example.SpringBootApplication_Novartis_Assignment.entity.ImgurData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgurDataRepository extends JpaRepository<ImgurData, Long> {
}
