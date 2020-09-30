package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

/**
 * ReplyDetails
 * @author Narasimha Reddy Guthireddy
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-09-20T00:54:17.931Z")


@Builder
public class ReplyDetails {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("author")
    private String author = null;

    @JsonProperty("message")
    private String message = null;


    /**
     * Get id
     *
     * @return id
     **/


    public Long getId() {
        return id;
    }

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
