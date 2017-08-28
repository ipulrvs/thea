package com.adsmoe.monolith.service;

import com.adsmoe.monolith.domain.QTodoItem;
import com.adsmoe.monolith.domain.TodoItem;
import com.adsmoe.monolith.repository.TodoItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TodoItem.
 */
@Service
@Transactional
public class TodoItemService {

    private final Logger log = LoggerFactory.getLogger(TodoItemService.class);

    private final TodoItemRepository todoItemRepository;

    private QTodoItem qTodoItem = QTodoItem.todoItem;

    public TodoItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    /**
     * Save a todoItem.
     *
     * @param todoItem the entity to save
     * @return the persisted entity
     */
    public TodoItem save(TodoItem todoItem) {
        log.debug("Request to save TodoItem : {}", todoItem);
        return todoItemRepository.save(todoItem);
    }

    /**
     *  Get all the todoItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TodoItem> findAll(Pageable pageable) {
        log.debug("Request to get all TodoItems");
        return todoItemRepository.findAll(pageable);
    }

    /**
     *  Get one todoItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TodoItem findOne(Long id) {
        log.debug("Request to get TodoItem : {}", id);
        return todoItemRepository.findOne(id);
    }

    /**
     *  Delete the  todoItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TodoItem : {}", id);
        todoItemRepository.delete(id);
    }
}
