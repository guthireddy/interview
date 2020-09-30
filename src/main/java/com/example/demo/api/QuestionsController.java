package com.example.demo.api;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
	
import com.example.demo.service.QuestionService;
import com.example.demo.model.MessageQuestionReplyBody;
import com.example.demo.model.messageReplyResponse;
import com.example.demo.model.QuestionDetails;
import com.example.demo.model.QuestionsResponse;

import lombok.RequiredArgsConstructor;

/**
 * @author Narasimha Reddy Guthireddy
 *
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen")
/**
 * QuestionsController component implements all rest services.
 * This component handovers the actual implementation to the service {@link QuestionService}
 * The service is injected via Constructor.
 * @generated SwaggerIO.
 * @author Narasimha Reddy Guthireddy
 */
@RestController
@RequiredArgsConstructor
public class QuestionsController {

    private static final Logger log = LoggerFactory.getLogger(QuestionsController.class);

    private final QuestionService service;

    /**
     * registerQuestion endpoint definition.
     *
     * @param body question request body of type {@link MessageQuestionReplyBody}.
     * @return QuestionDetails of the new question Added.
     */
    @RequestMapping(value = "/questions",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<QuestionDetails> addQuestion(@Valid @RequestBody(required = true) MessageQuestionReplyBody body) {
    	log.info("Calling registerQuestion method");
        return service.registerQuestion(body);
    }
    /**
     * messageReply endpoint definition.
     *
     * @param requestBody reply request body of type {@link MessageQuestionReplyBody}.
     * @param questionId  input question to which the given reply is added.
     * @return ReplyDetails of the new reply Added of Type {@link messageReplyResponse}
     */
    @RequestMapping(value = "/questions/{questionId}/reply",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<messageReplyResponse> createReply(@PathVariable("questionId") Long questionId, @Valid @RequestBody MessageQuestionReplyBody requestBody) {
    	log.info("Calling messageReply method");
        return service.messageReply(questionId, requestBody);
    }
    /**
     * getListOfQuestions endpoint definition.
     *
     * @return list of all questions in the database of type {@link List<QuestionDetails>}
     */
    @RequestMapping(value = "/questions",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<List<QuestionDetails>> getListOfQuestions() {
    	log.info("Calling getListOfQuestions method");
        return service.getListOfQuestions();
    }
    /**
     * getQuestion endpoint definition.
     *
     * @param questionId input questionId to extract the details.
     * @return questionDetails of the input question requested of type{@link QuestionsResponse}
     */
    @RequestMapping(value = "/questions/{questionId}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
  public ResponseEntity<QuestionsResponse> getQuestionDetails(@PathVariable("questionId") Long questionId) {
    	log.info("Calling getQuestion method");
        return service.getQuestion(questionId);

    }

}
