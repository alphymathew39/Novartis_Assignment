package alphy.example.SpringBootApplication_Novartis_Assignment.controller;

import alphy.example.SpringBootApplication_Novartis_Assignment.service.ImgurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {

    @Mock
    private ImgurService imgurService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUploadImageSuccess() {
        // Mocking the ImgurService behavior
        when(imgurService.uploadImage(any(MultipartFile.class))).thenReturn("12345");
        // Create a mock MultipartFile
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        // Test the uploadImage endpoint
        ResponseEntity<String> response = imageController.uploadImage(mockFile);
        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Image uploaded successfully. Image ID: 12345", response.getBody());
    }

    @Test
    void testUploadImageFailure() {
        when(imgurService.uploadImage(any(MultipartFile.class))).thenReturn(null);
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        ResponseEntity<String> response = imageController.uploadImage(mockFile);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to upload image.", response.getBody());
    }

    @Test
    void testViewImageSuccess() {
        when(imgurService.viewImage("12345")).thenReturn("http://example.com/image.jpg");
        ResponseEntity<String> response = imageController.viewImage("12345");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://example.com/image.jpg", response.getBody());
    }

    @Test
    void testViewImageFailure() {
        when(imgurService.viewImage("12345")).thenReturn(null);
        ResponseEntity<String> response = imageController.viewImage("12345");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteImageSuccess() {
        when(imgurService.deleteImage("12345")).thenReturn(true);
        ResponseEntity<String> response = imageController.deleteImage("12345");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Image deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteImageFailure() {
        when(imgurService.deleteImage("12345")).thenReturn(false);
        ResponseEntity<String> response = imageController.deleteImage("12345");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to delete image.", response.getBody());
    }
}
