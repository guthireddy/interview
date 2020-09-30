package com.example.demo.service;

import com.example.demo.exceptions.QuestionsException;
import com.example.demo.model.messageReplyResponse;
import com.example.demo.model.MessageQuestionReplyBody;
import com.example.demo.model.QuestionDetails;
import com.example.demo.model.QuestionsResponse;
import com.example.demo.persistence.QuestionsEntity;
import com.example.demo.persistence.QuestionsRepository;
import com.example.demo.service.QuestionService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test for delegate {@link QuestionService} which tests all code logics defined in the public methods..
 *
 * @author Narasimha Reddy Guthireddy
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService controllerDelegate;
    @Mock
    private QuestionsRepository repository;

    /**
     * Test add new question with valid input to test success flow.
     *
     * @result New Question is created with input data.
     */
    @Test
    public void registerQuestion_WhenInputIsValid_ReturnNewQuestionCreated() {
        when(repository.save(any())).thenReturn(createQuestionsEntity());
        ResponseEntity<QuestionDetails> response = controllerDelegate.registerQuestion(getValidRequestBody());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(101l);
    }

    /**
     * Test add new question when input has invalid author input.
     *
     * @result method throws exception and the same is validated.
     */
    @Test
    public void registerQuestion_WhenInputAuthorIsEmpty_ThrowsException() {
        assertThatThrownBy(() -> controllerDelegate.registerQuestion(getRequestWithEmptyAuthor()))
                .hasMessageContaining("Invalid input. Author cannot be empty or null")
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test add new question when input has invalid message input.
     *
     * @result method throws exception and the same is validated.
     */
    @Test
    public void registerQuestion_WhenInputMessageIsEmpty_ThrowsException() {
        assertThatThrownBy(() -> controllerDelegate.registerQuestion(getRequestWithEmptyMessage()))
                .hasMessageContaining("Invalid input. Message cannot be empty or null")
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test add new question when there is a db operation failure.
     *
     * @result method throws exception and the same is validated.
     */
    @Test
    public void registerQuestion_WhenRepositoryFails_ThrowsException() {
        when(repository.save(any())).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.registerQuestion(getValidRequestBody()))
                .hasMessageContaining("Exception occurred while saving Question to Database")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test add new reply when input is valid and new reply is created.
     *
     * @result new reply created and the same is verified here.
     */
    @Test
    public void messageReply_WhenInputIsValid_createsNewReply() {
        when(repository.save(any())).thenReturn(createReplyEntity());
        when(repository.findById(any())).thenReturn(Optional.of(createReplyEntity()));
        ResponseEntity<messageReplyResponse> response = controllerDelegate.messageReply(10l, getValidRequestBody());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(101l);
        assertThat(response.getBody().getQuestionId()).isEqualTo(10l);
    }

    /**
     * Test add new reply when input question is not found.
     *
     * @result throws error and the same is validated.
     */
    @Test
    public void messageReply_whenInputQuestionIsNotFound_throwsException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controllerDelegate.messageReply(10l, getValidRequestBody()))
                .hasMessageContaining("Reply Not Found.")
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test add new reply when input has invalid author input.
     *
     * @result throws validation error and the result is validated.
     */
    @Test
    public void messageReply_whenInputAuthorIsEmpty_throwsException() {
        assertThatThrownBy(() -> controllerDelegate.messageReply(10l, getRequestWithEmptyAuthor()))
                .hasMessageContaining("Invalid input. Author cannot be empty or null")
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test add new reply when input has invalid message input.
     *
     * @result throws validation error and the result is validated.
     */
    @Test
    public void messageReply_WhenInputMessageIsEmpty_ThrowsException() {
        assertThatThrownBy(() -> controllerDelegate.messageReply(10l, getRequestWithEmptyMessage()))
                .hasMessageContaining("Invalid input. Message cannot be empty or null")
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test add new reply when jpa error is thrown.
     *
     * @result throws jpa exception and the result is validated.
     */
    @Test
    public void messageReply_WhenJPAExceptionOccurs_ThrowsException() {
        when(repository.findById(any())).thenReturn(Optional.of(createReplyEntity().getQuestion()));
        when(repository.save(any())).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.messageReply(10l, getValidRequestBody()))
                .hasMessageContaining("Reply Not Found")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test getListOfQuestion service when no error occurs.
     *
     * @result return a list of questions and the result is validated.
     */
    @Test
    public void getListOfQuestions_whenSuccessful_returnsListOfQuestions() {
        when(repository.findByParentQuestionIdIsNull())
                .thenReturn(Collections.singletonList(createQuestionsEntity()));
        ResponseEntity<List<QuestionDetails>> response = controllerDelegate.getListOfQuestions();
        assertThat(response).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0)).isNotNull();
        assertThat(response.getBody().get(0).getId()).isEqualTo(101l);
    }

    /**
     * Test getListofQuestions service and the db throws error.
     *
     * @result exception is thrown by the method.
     */
    @Test
    public void getListOfQuestions_whenJPAExceptionOccurs_throwsException() {
        when(repository.findByParentQuestionIdIsNull()).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.getListOfQuestions())
                .hasMessageContaining("Exception occurred while reading Questions from Database")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test getQuestionDetails api with valid input.
     *
     * @result return the question details for input questionId.
     */
    @Test
    public void getQuestion_whenInputIsValid_returnsValidResponse() {
        when(repository.findById(any())).thenReturn(Optional.of(createQuestionsEntity()));
        ResponseEntity<QuestionsResponse> response = controllerDelegate.getQuestion(10l);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(101l);
        assertThat(response.getBody().getReplies().size()).isEqualTo(1);
    }

    /**
     * Test getQuestionDetails api when  input question id does not exist.
     *
     * @result not found error is thrown by method.
     */
    @Test
    public void getQuestion_whenInputQuestionIsNotFound_throwsException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controllerDelegate.getQuestion(10l))
                .hasMessageContaining("Exception occurred while reading question details.")
                .isInstanceOf(QuestionsException.class);
    }

    /**
     * Test getQuestionDetails api when a db error occurs.
     *
     * @result JPA exception is thrown by the method.
     */
    @Test
    public void getQuestion_whenJPAExceptionOccurs_throwsException() {
        when(repository.findById(any())).thenThrow(new JpaSystemException(new RuntimeException("sample exception")));
        assertThatThrownBy(() -> controllerDelegate.getQuestion(10l))
                .hasMessageContaining("Exception occurred while reading question details")
                .hasCauseExactlyInstanceOf(JpaSystemException.class)
                .isInstanceOf(QuestionsException.class);
    }
    


    public static QuestionsEntity createQuestionsEntity() {
        return QuestionsEntity.builder()
                .message("first message")
                .author("sample")
                .id(101l)
                .replies(Collections.singletonList(createReplyEntity()))
                .build();
    }

    public static MessageQuestionReplyBody getRequestWithEmptyAuthor() {
        return MessageQuestionReplyBody.builder()
                .message("sample message")
                .build();
    }

    public static MessageQuestionReplyBody getRequestWithEmptyMessage() {
        return MessageQuestionReplyBody.builder()
                .author("Reddy")
                .build();
    }

    public static MessageQuestionReplyBody getValidRequestBody() {
        return MessageQuestionReplyBody.builder()
                .author("Reddy")
                .message("sample message")
                .build();
    }

    public static QuestionsEntity createReplyEntity() {
        return QuestionsEntity.builder()
                .message("first message")
                .author("sample")
                .parentQuestionId(10l)
                .id(101l)
                .question(QuestionsEntity.builder().id(10l).build())
                .build();
    }

    
}
