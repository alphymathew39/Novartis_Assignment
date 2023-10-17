package alphy.example.SpringBootApplication_Novartis_Assignment.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgurImageData {
    @JsonProperty("link")
    private String link;

    public String getLink() {
        return link;
    }
}
