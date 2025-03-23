package org.example.socialbe.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RestErrorException extends RuntimeException {
    private int httpStatus;
    private String body;
}
