package com.example.demo.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * JPA Entity for Questions and Replies to Store in DB with
 * a self reference by replies to its corresponding questions.
 *
 * @author Narasimha Reddy Guthireddy
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionsEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String author;
    private String message;
    private Long parentQuestionId;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<QuestionsEntity> replies;

    @JoinColumn(name = "parentQuestionId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionsEntity question;
}
