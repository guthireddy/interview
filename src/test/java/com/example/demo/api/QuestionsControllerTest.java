package com.example.demo.api;


import com.example.demo.exceptions.QuestionsErrorResponse;
import com.example.demo.model.MessageQuestionReplyBody;
import com.example.demo.model.messageReplyResponse;
import com.example.demo.model.QuestionDetails;
import com.example.demo.model.QuestionsResponse;
import com.example.demo.persistence.QuestionsEntity;
import com.example.demo.persistence.QuestionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests for the API endpoints defined in {@link com.example.demo.forum.api.QuestionsApi}
 * H2 DB is used as backend db.
 *
 * @author Narasimha Reddy Guthireddy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    @Autowired
    private QuestionsRepository repository;
    private QuestionsEntity sampleQuestion;

    /**
     * Initializes the test data before any tests are run.
     * Configures the rest content-type and creates an initial question to use in tests.
     */
    @Before
    public void init() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        sampleQuestion = repository.save(QuestionsEntity.builder()
                .message("test message")
                .author("test user")
                .build());
    }

    /**
     * Create a new Question in the forum.
     *
     * @result New Question is created with input data.
     */
    @Test
    public void registerQuestion_whenInputIsValid_ReturnsNewQuestionCreated() {
        HttpEntity<MessageQuestionReplyBody> entity = new HttpEntity<MessageQuestionReplyBody>(MessageQuestionReplyBody.builder()
                .message("test message")
                .author("Narasimha")
                .build(),
                headers);
        ResponseEntity<QuestionDetails> response = restTemplate.exchange(
                "/questions",
                HttpMethod.POST, entity, QuestionDetails.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isGreaterThan(1l);
        assertThat(response.getBody().getAuthor()).isEqualTo("Narasimha");
    }

    /**
     * Exception occurs while creating the new question.
     *
     * @result Response contains a validation error. Also tests the Exception handler defined.
     */
    @Test
    public void registerQuestion_whenInputAuthorIsEmpty_ReturnsErrorResponse() {
        HttpEntity<MessageQuestionReplyBody> entity = new HttpEntity<MessageQuestionReplyBody>(MessageQuestionReplyBody.builder()
                .message("test message")
                .author("")
                .build(),
                headers);
        ResponseEntity<QuestionsErrorResponse> response = restTemplate.exchange(
                "/questions",
                HttpMethod.POST, entity, QuestionsErrorResponse.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERROR008");
        assertThat(response.getBody().getMessage()).contains("Invalid input. Author cannot be empty or null.");
    }

    /**
     * Create new reply with the input data.
     *
     * @result New reply is created for given question.
     */
    @Test
    public void messageReply_whenInputIsValid_ReturnsNewReplyCreated() {
        HttpEntity<MessageQuestionReplyBody> entity = new HttpEntity<>(MessageQuestionReplyBody.builder()
                .message("test message")
                .author("Narasimha")
                .build(),
                headers);
        ResponseEntity<messageReplyResponse> response = restTemplate.exchange(
                "/questions/" + sampleQuestion.getId() + "/reply",
                HttpMethod.POST, entity, messageReplyResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAuthor()).isEqualTo("Narasimha");
        assertThat(response.getBody().getMessage()).contains("test");
        assertThat(response.getBody().getQuestionId()).isEqualTo(sampleQuestion.getId());
    }

    /**
     * Tests exception scenario for createReply endpoint.
     * Input data has a validation error.
     *
     * @result Error response is received with a validation error.
     */
    @Test
    public void messageReply_whenInputMessageIsEmpty_ReturnsErrorResponse() {
        HttpEntity<MessageQuestionReplyBody> entity = new HttpEntity<>(MessageQuestionReplyBody.builder()
                .message("")
                .author("Narasimha")
                .build(),
                headers);
        ResponseEntity<QuestionsErrorResponse> response = restTemplate.exchange(
                "/questions/" + sampleQuestion.getId() + "/reply",
                HttpMethod.POST, entity, QuestionsErrorResponse.class);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERROR009");
        assertThat(response.getBody().getMessage()).contains("Invalid input. Message cannot be empty or null");
    }

    /**
     * Test getQuestions to receive all questions saved in DB.
     *
     * @result Returns a list of questions saved in DB.
     */
    @Test
    public void getListOfQuestions_whenInputIsValid_ReturnsQuestionsList() {
        HttpEntity<?> entity = new HttpEntity<>(null,
                headers);
        ResponseEntity<List<QuestionDetails>> response = restTemplate.exchange(
                "/questions/",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<QuestionDetails>>() {
                });
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);
        assertThat(response.getBody().get(0)).isNotNull();
    }

    /**
     * Request to get input question details. Input takes existing questionId.
     *
     * @result Response is validated for question details requested by questionID.
     */
    @Test
    public void getQuestion_whenInputIsValid_ReturnsQuestionsDetails() {
        HttpEntity<?> entity = new HttpEntity<>(null,
                headers);
        ResponseEntity<QuestionsResponse> response = restTemplate.exchange(
                "/questions/" + sampleQuestion.getId(),
                HttpMethod.GET, entity, QuestionsResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getReplies().size()).isGreaterThanOrEqualTo(0);
        assertThat(response.getBody().getId()).isEqualTo(sampleQuestion.getId());
        assertThat(response.getBody().getMessage()).isEqualTo(sampleQuestion.getMessage());
    }
}
