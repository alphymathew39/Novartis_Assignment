package alphy.example.SpringBootApplication_Novartis_Assignment.service;

import alphy.example.SpringBootApplication_Novartis_Assignment.exception.ImgurAuthorizationException;
import alphy.example.SpringBootApplication_Novartis_Assignment.exception.ImgurClientException;
import alphy.example.SpringBootApplication_Novartis_Assignment.exception.ImgurServiceException;
import alphy.example.SpringBootApplication_Novartis_Assignment.response.ImgurViewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImgurServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ImgurService imgurService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUploadImageSuccess() throws Exception {
        // Mocking the successful image upload response
        String fakeResponse = "{ \"data\": { \"id\": \"fakeImageId\" } }";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(fakeResponse, HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))
        ).thenReturn(responseEntity);
        MockMultipartFile fakeImage = new MockMultipartFile("image", "fake-image.jpg", "image/jpeg", "fakeImageData".getBytes());
        String imageId = imgurService.uploadImage(fakeImage);
        assertNotNull(imageId);
        assertEquals("fakeImageId", imageId);
    }

    @Test
    void testUploadImageFailure() throws Exception {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        )).thenReturn(responseEntity);
        MockMultipartFile fakeImage = new MockMultipartFile("image", "fake-image.jpg", "image/jpeg", "fakeImageData".getBytes());
        String imageId = imgurService.uploadImage(fakeImage);
        assertNull(imageId);
    }

    @Test
    void testViewImageFailure() {
        ResponseEntity<ImgurViewResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))
        ).thenReturn(responseEntity);
        String imageUrl = imgurService.viewImage("fakeImageId");
        assertNull(imageUrl);
    }

    @Test
    void testDeleteImageSuccess() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        )).thenReturn(responseEntity);
        boolean result = imgurService.deleteImage("fakeImageId");
        assertTrue(result);
    }

    @Test
    void testDeleteImageSuccessNoContent() {
        // Mocking a successful image deletion response with no content (204 No Content)
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        )).thenReturn(responseEntity);
        boolean result = imgurService.deleteImage("fakeImageId");
        assertTrue(result);
    }

    @Test
    void testDeleteImageFailureAuthorization() {
        // Mocking a 403 Forbidden response
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.FORBIDDEN);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        )).thenThrow(exception);
        assertThrows(ImgurAuthorizationException.class, () -> imgurService.deleteImage("fakeImageId"));
    }

    @Test
    void testDeleteImageFailureClient() {
        // Mocking a client error response
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        )).thenThrow(exception);
        assertThrows(ImgurClientException.class, () -> imgurService.deleteImage("fakeImageId"));
    }

    @Test
    void testDeleteImageFailureService() {
        // Mocking a general service error
        Exception exception = new RuntimeException("Some error");
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        )).thenThrow(exception);
        assertThrows(ImgurServiceException.class, () -> imgurService.deleteImage("fakeImageId"));
    }
}
