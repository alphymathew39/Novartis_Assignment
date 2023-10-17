package alphy.example.SpringBootApplication_Novartis_Assignment.exception;

public class ImgurServiceException extends RuntimeException {
    public ImgurServiceException(String message) {
        super(message);
    }
}