package com.adsmoe.monolith.repository;

import com.adsmoe.monolith.domain.Todo;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Todo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoRepository extends JpaRepository<Todo,Long>, QueryDslPredicateExecutor<Todo> {

}
