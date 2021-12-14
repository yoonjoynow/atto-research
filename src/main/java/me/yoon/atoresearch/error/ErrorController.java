package me.yoon.atoresearch.error;

import me.yoon.atoresearch.error.exception.DuplicatedHostException;
import me.yoon.atoresearch.error.exception.ExceedMaxLimitException;
import me.yoon.atoresearch.error.exception.UnregisteredHostException;
import me.yoon.atoresearch.error.exception.HostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(HostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHostNotFoundException(HostNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.HOST_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnregisteredHostException.class)
    public ResponseEntity<ErrorResponse> handleUnknownHostException(UnregisteredHostException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNREGISTERED_HOST);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicatedHostException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedHostException(DuplicatedHostException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATED_HOST);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExceedMaxLimitException.class)
    public ResponseEntity<ErrorResponse> handleExceedMaxLimitException(ExceedMaxLimitException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.EXCEED_MAX_LIMIT);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
