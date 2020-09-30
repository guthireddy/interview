package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

/**
 * MessageQuestionReplyBody
 * @author Narasimha Reddy Guthireddy
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-09-20T00:54:17.931Z")

@Builder
public class MessageQuestionReplyBody {
    @JsonProperty("author")
    private String author = null;

    @JsonProperty("message")
    private String message = null;


    /**
     * Get author
     *
     * @return author
     **/


    public String getAuthor() {
        return author;
    }


    /**
     * Get message
     *
     * @return message
     **/


    public String getMessage() {
        return message;
    }


}

