package com.yu.jangtari.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE("yu1000", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR("yu1001", "서버 내부 오류가 발생했습니다."),

    // =================================================================================================================
    // security, JWT 관련 에러 20XX
    // =================================================================================================================
    ACCESS_DENIED_ERROR("yu2001", "접근 권한이 없습니다."),
    UNAUTHORIZED_ERROR("yu2002", "인증되지 않은 요청입니다."),
    JWT_TIMEOUT_ERROR("yu2003", "토큰이 만료되었습니다."),
    JWT_VALIDATION_ERROR("yu2004", "올바르지 않은 토큰입니다."),

    // =================================================================================================================
    // 회원 관련 에러 30XX
    // =================================================================================================================
    LOGIN_ERROR("yu3000", "아이디 혹은 비밀번호가 맞지 않습니다."),
    JOIN_ERROR("yu3001", "회원가입 중 오류가 발생했습니다."),
    MEMBER_NOT_FOUND_ERROR("yu3002", "회원 정보가 존재하지 않습니다."),
    ADMIN_DELETE_ERROR("yu3003", "ADMIN 회원은 삭제가 불가능힙니다."),
    DUPLICATED_MEMBER_ERROR("yu3004", "이미 존재하는 회원입니다."),

    // =================================================================================================================
    // 카테고리 관련 에러 40XX
    // =================================================================================================================
    CATEGORY_NOT_FOUND_ERROR("yu4000", "해당 카테고리가 존재하지 않습니다."),

    COMMENT_NOT_FOUND("yu006", "No Such Comment"),
    PICTURE_NOT_FOUND("yu008", "No Such Picture"),
    HASHTAG_NOT_FOUND("yu009", "No Such Hashtag"),
    POST_NOT_FOUND("yu010", "No Such Post"),
    SEARCH_TYPE_ERROR("yu013", "No Such SearchType"),
    JANGTARI_DELETE_ERROR("yu014", "Nobody Can Delete Jangtari"),
    INVALID_TOKEN_ERROR("yu015", "Invalid Token"),
    DUPLICATE_USER_ERROR("yu017", "Duplicate User Error"),
    RE_LOGIN_ERROR("yu018", "Need to reLogin"),


    FILE_ERROR("yu011", "File Task Error"),
    GOOGLE_DRIVE_ERROR("yu012", "Google Dirve Error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
