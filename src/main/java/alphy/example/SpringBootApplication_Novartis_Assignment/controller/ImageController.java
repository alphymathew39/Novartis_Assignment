package alphy.example.SpringBootApplication_Novartis_Assignment.controller;

import alphy.example.SpringBootApplication_Novartis_Assignment.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImgurService imgurService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.info("Uploading image...");
        String imageId = imgurService.uploadImage(file);

        if (imageId != null) {
            logger.info("Image uploaded successfully. Image ID: {}", imageId);
            return ResponseEntity.ok("Image uploaded successfully. Image ID: " + imageId);
        } else {
            logger.error("Failed to upload image.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/view/{imageId}")
    public ResponseEntity<String> viewImage(@PathVariable String imageId) {
        logger.info("Viewing image with ID: {}", imageId);
        String imageUrl = imgurService.viewImage(imageId);

        if (imageUrl != null) {
            logger.info("Image viewed successfully.");
            return ResponseEntity.ok(imageUrl);
        } else {
            logger.error("Image not found.");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable String imageId) {
        logger.info("Deleting image with ID: {}", imageId);

        if (imgurService.deleteImage(imageId)) {
            logger.info("Image deleted successfully.");
            return ResponseEntity.ok("Image deleted successfully.");
        } else {
            logger.error("Failed to delete image.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image.");
        }
    }
}
