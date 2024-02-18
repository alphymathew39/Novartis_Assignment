package alphy.example.SpringBootApplication_Novartis_Assignment.entity;

import alphy.example.SpringBootApplication_Novartis_Assignment.ImgurIdDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "imgur_data")
//entity classhhhhh
public class ImgurData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = ImgurIdDeserializer.class)
    private Long id;

    private String imgurId; // This field represents the image ID from Imgur

    private String imgurLink;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // User associated with this image
}