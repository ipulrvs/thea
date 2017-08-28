package com.adsmoe.monolith.service;

import com.adsmoe.monolith.domain.QTodo;
import com.adsmoe.monolith.domain.Todo;
import com.adsmoe.monolith.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;


/**
 * Service Implementation for managing Todo.
 */
@Service
@Transactional
public class TodoService {

    private final Logger log = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    private QTodo qTodo = QTodo.todo;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Save a todo.
     *
     * @param todo the entity to save
     * @return the persisted entity
     */
    public Todo save(Todo todo) {
        log.debug("Request to save Todo : {}", todo);
        return todoRepository.save(todo);
    }

    /**
     *  Get all the todos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Todo> findAll(Pageable pageable) {
        log.debug("Request to get all Todos");
        return todoRepository.findAll(pageable);
    }

    /**
     *  Get one todo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Todo findOne(Long id) {
        log.debug("Request to get Todo : {}", id);
        return todoRepository.findOne(id);
    }

    /**
     *  Delete the  todo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Todo : {}", id);
        todoRepository.delete(id);
    }
}
