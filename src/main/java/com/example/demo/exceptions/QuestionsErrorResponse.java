package com.example.demo.exceptions;

import lombok.Builder;
import lombok.Getter;

/**
 * Error Response definition to send to the requestor if there an error during processing of a request.
 * This is used by ExceptionHandler {@link com.example.demo.QuestionsExceptionHandler.exceptions.QuestionsApiExceptionHandler}.
 *
 * @author Narasimha Reddy Guthireddy
 */
@Builder
@Getter
public class QuestionsErrorResponse {
    private String errorCode;
    private String message;
    private String description;
}
