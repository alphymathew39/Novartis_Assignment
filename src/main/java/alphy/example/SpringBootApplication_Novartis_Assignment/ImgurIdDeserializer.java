package alphy.example.SpringBootApplication_Novartis_Assignment;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ImgurIdDeserializer extends StdDeserializer<Long> {
    public ImgurIdDeserializer() {
        super(Long.class);
    }

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String imgurId = p.getText();
        try {
            // Attempt to convert the imgurId String to a Long
            return Long.parseLong(imgurId);
        } catch (NumberFormatException e) {
            // Handle non-numeric values, e.g., return null or a default value
            return null; // You can choose to return null for non-numeric imgur IDs
        }
    }
}
