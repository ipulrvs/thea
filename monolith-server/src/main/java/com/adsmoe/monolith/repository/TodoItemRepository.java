package com.adsmoe.monolith.repository;

import com.adsmoe.monolith.domain.TodoItem;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TodoItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem,Long>, QueryDslPredicateExecutor<TodoItem> {

}
