package com.example.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Runtime Exception type used by all components to wrap exception occurred during processing a request.
 *
 * @author Narasimha Reddy Guthireddy
 */
@Getter
public class QuestionsException extends RuntimeException {

    private String errorCode;
    private HttpStatus status;

    /**
     * Creates a QuestionsApiException with the parameters provided.
     * the specified size.
     *
     * @param message   exception message to send to user.
     * @param cause     actual exception if any ot pass to super class {@link RuntimeException}
     * @param errorCode unique error code identifies a scenario in a component.
     * @param status    httpStatus to send back to the user.
     */
    public QuestionsException(String message, Throwable cause, String errorCode, HttpStatus status) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }
}
