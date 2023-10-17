package alphy.example.SpringBootApplication_Novartis_Assignment.service;

import alphy.example.SpringBootApplication_Novartis_Assignment.exception.ImgurAuthorizationException;
import alphy.example.SpringBootApplication_Novartis_Assignment.exception.ImgurClientException;
import alphy.example.SpringBootApplication_Novartis_Assignment.exception.ImgurServiceException;
import alphy.example.SpringBootApplication_Novartis_Assignment.response.ImgurViewResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImgurService {
    private static final Logger logger = LoggerFactory.getLogger(ImgurService.class);

    @Value("${imgur.client-id}")
    private String clientId;

    private final String IMGUR_API_URL = "https://api.imgur.com/3/";

    private final RestTemplate restTemplate;

    public ImgurService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadImage(MultipartFile imageFile) {
        try {
            logger.info("Uploading image...");
            byte[] imageBytes = imageFile.getBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Client-ID " + clientId);

            HttpEntity<byte[]> request = new HttpEntity<>(imageBytes, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    IMGUR_API_URL + "image",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String imageId = jsonNode.get("data").get("id").asText();

                // Save imgurData to your repository or database
                // imgurDataRepository.save(imgurData);

                return imageId; // Return the imageId
            }
        } catch (IOException e) {
            logger.error("Error uploading image: " + e.getMessage(), e);
            // Handle the exception
        }

        return null;
    }


    public String viewImage(String imageId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ImgurViewResponse> response = restTemplate.exchange(
                IMGUR_API_URL + "image/" + imageId,
                HttpMethod.GET,
                request,
                ImgurViewResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            ImgurViewResponse viewResponse = response.getBody();
            logger.info("Viewing image: " + viewResponse.getData().getLink());
            return viewResponse.getData().getLink();
        }

        return null;
    }

    public boolean deleteImage(String imageId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);

        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    IMGUR_API_URL + "image/" + imageId,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Image deleted successfully.");
                return true;
            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                logger.info("Image deleted successfully.");
                // Handle successful deletion with no content (204 No Content)
                return true;
            } else {
                // Handle other successful but non-OK status codes
                return false;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                // Handle 403 Forbidden error (Unauthorized)
                logger.error("Permission denied for image deletion. Check token permissions.");
                throw new ImgurAuthorizationException("Permission denied for image deletion. Check token permissions.");
            } else {
                logger.error("Error during image deletion: " + e.getMessage(), e);
                // Handle other HTTP client errors
                throw new ImgurClientException("Error during image deletion: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error during image deletion: " + e.getMessage(), e);
            // Handle other exceptions
            throw new ImgurServiceException("Error during image deletion: " + e.getMessage());
        }
    }
}
