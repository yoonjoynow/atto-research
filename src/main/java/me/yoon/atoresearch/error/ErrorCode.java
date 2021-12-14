package me.yoon.atoresearch.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    UNREGISTERED_HOST(HttpStatus.BAD_REQUEST, "잘못된 호스트 입력값입니다."),
    DUPLICATED_HOST(HttpStatus.CONFLICT, "이미 등록된 호스트입니다."),
    HOST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 호스트를 찾을 수 없습니다."),
    EXCEED_MAX_LIMIT(HttpStatus.BAD_REQUEST, "호스트 저장 최대 갯수가 초과되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
