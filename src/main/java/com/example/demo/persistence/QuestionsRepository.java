package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for QuestionsEntity.
 * Defines method to get all Questions which are not replies.
 *
 * @author Narasimha Reddy Guthireddy
 */
public interface QuestionsRepository  extends JpaRepository<QuestionsEntity, Long> {
    List<QuestionsEntity> findByParentQuestionIdIsNull();
}
