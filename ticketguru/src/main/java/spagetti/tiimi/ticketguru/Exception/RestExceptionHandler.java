package spagetti.tiimi.ticketguru.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<Map<String, Object>> RestBadRequest(BadRequestException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "error", "Bad Request",
                                                "message", exception.getMessage()));

        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<Map<String, Object>> RestNotFound(NotFoundException exception) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                                "status", HttpStatus.NOT_FOUND.value(),
                                                "error", "Not Found",
                                                "message", exception.getMessage()));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException exception) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of(
                                                "status", HttpStatus.FORBIDDEN.value(),
                                                "error", "Forbidden",
                                                "message", exception.getMessage()));
        }

        @ExceptionHandler(TicketAlreadyRedeemedException.class)
        public ResponseEntity<Map<String, Object>> handleTicketAlreadyRedeemed(
                        TicketAlreadyRedeemedException exception) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of(
                                                "status", HttpStatus.CONFLICT.value(),
                                                "error", "Already used",
                                                "message", exception.getMessage()));
        }

        // alemmat voi poistaa myöhemmin, jos tarpeettomia
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> RestArgumentNotValid(MethodArgumentNotValidException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "error", "Bad Request",
                                                "message", exception.getMessage()));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<Map<String, Object>> RestArgumentMismatch(MethodArgumentTypeMismatchException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "error", "Bad Request",
                                                "message", exception.getMessage()));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "error", "Bad Request",
                                                "message", exception.getMessage()));
        }

}
