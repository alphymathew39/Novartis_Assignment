package alphy.example.SpringBootApplication_Novartis_Assignment.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgurViewResponse {
    @JsonProperty("data")
    private ImgurImageData data;

    public ImgurImageData getData() {
        return data;
    }
}
