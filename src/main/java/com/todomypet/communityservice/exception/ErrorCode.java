package com.todomypet.communityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    POST_CONTENT_NULL(HttpStatus.BAD_REQUEST, "C001", "게시글 내용은 NULL이 될 수 없습니다."),
    POST_NOT_EXIST(HttpStatus.BAD_REQUEST, "C002", "존재하지 않는 게시글입니다."),
    DELETED_POST(HttpStatus.BAD_REQUEST, "C003", "삭제된 게시글에 대한 요청입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "C004", "권한이 없는 요청입니다."),
    ALREADY_EXISTS_RELATIONSHIP(HttpStatus.BAD_REQUEST, "C005", "이미 해당 관계가 존재합니다."),
    NOT_EXISTS_RELATIONSHIP(HttpStatus.BAD_REQUEST, "C006", "존재하지 않는 관계에 대한 삭제 요청입니다."),
    REPLY_NOT_EXISTS(HttpStatus.BAD_REQUEST, "C007" , "존재하지 않는 댓글입니다."),
    DELETED_REPLY(HttpStatus.BAD_REQUEST, "C008", "삭제된 댓글에 대한 요청입니다."),
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "C009", "파일 업로드에 실패했습니다."),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "C010", "존재하지 않는 사용자입니다."),
    REPLY_CONTENT_NULL(HttpStatus.BAD_REQUEST, "C011", "댓글 내용은 NULL이 될 수 없습니다."),
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "C012", "메일 전송에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
