package ru.practicum.ewm.server.stats.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
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

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(List.of(getStackTrace(e)), e.getMessage(),
                "Incorrectly made request.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }
}
