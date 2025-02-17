package ar.edu.utn.frc.tup.lciii.exception;

import ar.edu.utn.frc.tup.lciii.dtos.common.ErrorApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleGenericException(Exception ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}