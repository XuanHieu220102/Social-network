package org.example.socialbe.exception;

import lombok.Getter;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.enums.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class LeaklessException extends RuntimeException {
    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    
    public LeaklessException(ErrorCode errorCode, String... args) {
        super(errorCode.format(args));
        this.errorCode = errorCode;
    }
    
    public LeaklessException(HttpStatus httpStatus, ErrorCode errorCode, String... args) {
        super(errorCode.format(args));
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(this.errorCode.name(), this.getMessage());
    }
}
