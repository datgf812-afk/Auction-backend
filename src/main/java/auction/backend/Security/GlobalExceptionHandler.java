package auction.backend.Security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllErrors(Exception e) {
        return ResponseEntity.badRequest().body("Lỗi hệ thống: " + e.getMessage());
    }
}