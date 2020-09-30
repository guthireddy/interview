package com.example.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;

/**
 * ExceptionHandler to handle all {@link QuestionsException} thrown by all components.
 * @author Narasimha Reddy Guthireddy
 */
@ControllerAdvice
public class QuestionsExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(QuestionsExceptionHandler.class);

    /** ControlleAdvice to handle all {@link QuestionsException} throws by the components.
     * @param   ex  {@link QuestionsException} thrown by the api.
     * @return  ResponseEntity of type {@link QuestionsErrorResponse} to send to the user.
     */
    @ExceptionHandler(QuestionsException.class)
    public ResponseEntity<?> handleApiException(QuestionsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                QuestionsErrorResponse.builder()
                        .errorCode(ex.getErrorCode())
                        .message(ex.getMessage())
                        .description(ofNullable(ex.getCause())
                                .map(cause -> cause.getLocalizedMessage())
                                .orElse(ex.getMessage())
                        )
                        .build(),
                ex.getStatus());
    }

    /** ControlleAdvice to handle all other unknown exceptions thrown by the api.
     * @param   ex  {@link Exception} thrown by the api.
     * @return  ResponseEntity of type {@link QuestionsErrorResponse} to send to the user with
     *          {@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                QuestionsErrorResponse.builder()
                        .errorCode("ERROR000")
                        .message(ex.getMessage())
                        .description("Unknown exception occurred while processing the request.")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
