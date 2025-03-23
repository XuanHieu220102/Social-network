package org.example.socialbe.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS ("Success"),
    OTHER ("%s"),
    INTERNAL_SERVER_ERROR ("Internal server error"),
    INVALID_CREDENTIAL ("Invalid username or password"),
    EMAIL_NOT_EXIST ("Email is incorrect"),
    INVALID_PASSWORD ("Invalid password"),
    USER_LOCKED ("User has been locked"),
    INVALID_ACCESS_TOKEN ("Invalid access token"),
    SESSION_EXPIRED ("Session expired"),
    ACCESS_DENIED ("Access denied"),
    USERNAME_EXISTS ("Username already exists"),
    RECORD_NOT_FOUND ("Record not found"),
    CREATE_FAILED ("Create failed"),
    UPDATE_FAILED ("Update failed"),
    DELETE_FAILED ("Delete failed"),
    VALUE_EXISTS ("Value exists"),
    INPUT_INVALID ("Input invalid"),
    PARSE_DATETIME_FAILED("Parse to datetime failed"),
    IMPORT_FAILED ("Import failed"),
    EXPORT_FAILED ("Export failed"),
    CITIZEN_IDENTITY_EXISTS ("CCCD đã tồn tại trên hệ thống"),
    PHONE_NUMBER_EXISTS("SĐT đã tồn tại trên hệ thống"),
    EXPORT_QUANTITY_INVALID("%s: Số lượng xuất kho lớn hơn số lượng tồn kho")
    ;
    private final String msg;

    ErrorCode(String msg) {
        this.msg = msg;
    }

    public String format(String... args) {
        return String.format(msg, (Object) args);
    }

}
