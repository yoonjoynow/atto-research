package me.yoon.atoresearch.error.exception;

public class ExceedMaxLimitException extends RuntimeException {

    public ExceedMaxLimitException(String message) {
        super(message);
    }
}
