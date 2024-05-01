package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ApplicationErrorHandler {

    private String getStackTrace(Exception e) {
        log.debug("Ошибка сервера:{}", e.getMessage());
        log.debug("stacktrace ошибки:{}", e.getStackTrace());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out, true, Charset.defaultCharset()));

        return out.toString(Charset.defaultCharset());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final SQLException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "Integrity constraint has been violated.", HttpStatus.CONFLICT, LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "Incorrectly made request.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "Incorrectly made request.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "The required object was not found.", HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleValidationException(final ValidationException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "For the requested operation the conditions are not met.", HttpStatus.CONFLICT, LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "For the requested operation the conditions are not met.", HttpStatus.CONFLICT, LocalDateTime.now()));
    }
}
