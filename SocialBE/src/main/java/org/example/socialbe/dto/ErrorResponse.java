package org.example.socialbe.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    String code;
    String message;
    Object data;

    public ErrorResponse() {}

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ErrorResponse success(Object data){
        return new ErrorResponse(String.valueOf(HttpStatus.OK.value()), "Success", data);
    }

    public static ErrorResponse recordNotFound(Object data){
        return new ErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), "Record Not Found", data);
    }

    public static ErrorResponse internalServerError(Exception ex){
        return new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage(), null);
    }
    public static ErrorResponse getError(HttpStatus httpStatus, Exception ex){
        return new ErrorResponse(String.valueOf(httpStatus.value()), ex.getMessage(), null);
    }
}
