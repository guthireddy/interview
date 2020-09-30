package com.example.demo.service;

import com.example.demo.exceptions.QuestionsException;
import com.example.demo.model.*;
import com.example.demo.persistence.QuestionsEntity;
import com.example.demo.persistence.QuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for {@link com.example.demo.api.QuestionsApiController} which implements the api endpoints.
 * {@link QuestionsRepository} is injected via constructor to provide support for jpa operations.
 *
 * @author Narasimha Reddy Guthireddy
 */

@Component
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionsRepository repository;

    /**
     * registerQuestion endpoint implementation. Input question details are used to create the new question in DB.
     *
     * @param body question request body of type {@link MessageQuestionReplyBody}.
     * @return questionDetails of type {@link QuestionDetails} of the new question Added.
     * @throws QuestionsException runtime exception if an error occurs during JPA operation.
     */
    public ResponseEntity<QuestionDetails> registerQuestion(MessageQuestionReplyBody body) {
        validateInputRequest(body);
        try {
            return ResponseEntity.ok(
                    Optional.of(repository.save(   //Save Question Entity to DB
                            QuestionsEntity.builder()  //Build Question Entity and pass it to save method.
                                    .author(body.getAuthor())
                                    .message(body.getMessage())
                                    .build()
                    ))
                            .map(entity -> QuestionDetails.builder()  // Use question Entity to create Response: Question Details.
                                    .author(entity.getAuthor())
                                    .id(entity.getId())
                                    .message(entity.getMessage())
                                    .replies(0L)
                                    .build())
                            .get()
            );
        } catch (Exception ex) {
            throw new QuestionsException("Exception occurred while saving Question to Database.", ex, "ERROR003", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * messageReply endpoint implementation. Input reply details are used to create the new reply in DB for a given question.
     *
     * @param requestBody question request body of type {@link MessageQuestionReplyBody}.
     * @param questionId  input questionId for the reply is saved.
     * @return postReplyResponse of type {@link messageReplyResponse} of the new reply Added.
     * @throws QuestionsException runtime exception if an error occurs during JPA operation or in input questionId not
     *                               present in the DB.
     */
    public ResponseEntity<messageReplyResponse> messageReply(Long questionId, MessageQuestionReplyBody requestBody) {
        validateInputRequest(requestBody);
        try {
            repository.findById(questionId)
                    .orElseThrow(
                            () -> new QuestionsException("Question Not Found.", null, "ERROR002", HttpStatus.NOT_FOUND)
                    );
            return ResponseEntity.ok(
                    Optional.of(repository.save(
                            QuestionsEntity.builder()
                                    .author(requestBody.getAuthor())
                                    .message(requestBody.getMessage())
                                    .parentQuestionId(questionId)
                                    .build()
                    ))
                            .map(entity -> messageReplyResponse.builder()
                                    .author(entity.getAuthor())
                                    .id(entity.getId())
                                    .message(entity.getMessage())
                                    .questionId(entity.getParentQuestionId())
                                    .build())
                            .get()
            );
        } catch (Exception ex) {
            throw new QuestionsException("Reply Not Found. ", ex, "ERROR001", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * getListOfQuestions endpoint implementation. Reads all questions in the Db and return to the requester.
     *
     * @return questionDetails of type {@link List<QuestionDetails>} of the new question Added.
     * @throws QuestionsException runtime exception if an error occurs during JPA operation.
     */
    public ResponseEntity<List<QuestionDetails>> getListOfQuestions() {

        try {
            return ResponseEntity.ok(repository.findByParentQuestionIdIsNull()
                    .stream()
                    .map(entity -> QuestionDetails.builder()
                            .message(entity.getMessage())
                            .id(entity.getId())
                            .author(entity.getAuthor())
                            .replies((long) entity.getReplies().size())
                            .build()
                    )
                    .collect(Collectors.toList()));
        } catch (Exception ex) {
            throw new QuestionsException("Exception occurred while reading Questions from Database.", ex, "ERROR0034", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * getQuestion endpoint implementation. Input reply details are used to create the new reply in DB for a given question.
     *
     * @param questionId of the question for which the details are requested.
     * @return questionsResponse of type {@link QuestionsResponse} of the question requested.
     * @throws QuestionsException runtime exception if an error occurs during JPA operation or in input questionId not
     *                               present in the DB.
     */
    public ResponseEntity<QuestionsResponse> getQuestion(@PathVariable("questionId") Long questionId) {
        try {
            return ResponseEntity.ok(
                    repository.findById(questionId)
                            .map(question -> QuestionsResponse.builder()
                                    .author(question.getAuthor())
                                    .id(question.getId())
                                    .message(question.getMessage())
                                    .replies(question.getReplies()
                                            .stream()
                                            .map(entity ->
                                                    ReplyDetails.builder()
                                                            .author(entity.getAuthor())
                                                            .message(entity.getMessage())
                                                            .id(entity.getId())
                                                            .build()
                                            )
                                            .collect(Collectors.toList())
                                    )
                                    .build())
                            .orElseThrow(
                                    () -> new QuestionsException("Question Not Found.", null, "ERROR006", HttpStatus.NOT_FOUND)
                            )
            );
        } catch (Exception ex) {
            throw new QuestionsException("Exception occurred while reading question details.", ex, "ERROR007", HttpStatus.NOT_FOUND);
        }
    }

    private void validateInputRequest(MessageQuestionReplyBody request) {
        if (StringUtils.isEmpty(request.getAuthor())) {
            throw new QuestionsException("Invalid input. Author cannot be empty or null.", null, "ERROR008", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (StringUtils.isEmpty(request.getMessage())) {
            throw new QuestionsException("Invalid input. Message cannot be empty or null.", null, "ERROR009", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
