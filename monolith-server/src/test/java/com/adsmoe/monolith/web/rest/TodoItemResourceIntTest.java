package com.adsmoe.monolith.web.rest;

import com.adsmoe.monolith.MonolithApp;

import com.adsmoe.monolith.domain.TodoItem;
import com.adsmoe.monolith.repository.TodoItemRepository;
import com.adsmoe.monolith.service.TodoItemService;
import com.adsmoe.monolith.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TodoItemResource REST controller.
 *
 * @see TodoItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonolithApp.class)
public class TodoItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private TodoItemService todoItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTodoItemMockMvc;

    private TodoItem todoItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TodoItemResource todoItemResource = new TodoItemResource(todoItemService);
        this.restTodoItemMockMvc = MockMvcBuilders.standaloneSetup(todoItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodoItem createEntity(EntityManager em) {
        TodoItem todoItem = new TodoItem()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .cost(DEFAULT_COST)
            .endDate(DEFAULT_END_DATE);
        return todoItem;
    }

    @Before
    public void initTest() {
        todoItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTodoItem() throws Exception {
        int databaseSizeBeforeCreate = todoItemRepository.findAll().size();

        // Create the TodoItem
        restTodoItemMockMvc.perform(post("/api/todo-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoItem)))
            .andExpect(status().isCreated());

        // Validate the TodoItem in the database
        List<TodoItem> todoItemList = todoItemRepository.findAll();
        assertThat(todoItemList).hasSize(databaseSizeBeforeCreate + 1);
        TodoItem testTodoItem = todoItemList.get(todoItemList.size() - 1);
        assertThat(testTodoItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTodoItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTodoItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTodoItem.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testTodoItem.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createTodoItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = todoItemRepository.findAll().size();

        // Create the TodoItem with an existing ID
        todoItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodoItemMockMvc.perform(post("/api/todo-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoItem)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TodoItem> todoItemList = todoItemRepository.findAll();
        assertThat(todoItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTodoItems() throws Exception {
        // Initialize the database
        todoItemRepository.saveAndFlush(todoItem);

        // Get all the todoItemList
        restTodoItemMockMvc.perform(get("/api/todo-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todoItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTodoItem() throws Exception {
        // Initialize the database
        todoItemRepository.saveAndFlush(todoItem);

        // Get the todoItem
        restTodoItemMockMvc.perform(get("/api/todo-items/{id}", todoItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(todoItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTodoItem() throws Exception {
        // Get the todoItem
        restTodoItemMockMvc.perform(get("/api/todo-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodoItem() throws Exception {
        // Initialize the database
        todoItemService.save(todoItem);

        int databaseSizeBeforeUpdate = todoItemRepository.findAll().size();

        // Update the todoItem
        TodoItem updatedTodoItem = todoItemRepository.findOne(todoItem.getId());
        updatedTodoItem
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .endDate(UPDATED_END_DATE);

        restTodoItemMockMvc.perform(put("/api/todo-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTodoItem)))
            .andExpect(status().isOk());

        // Validate the TodoItem in the database
        List<TodoItem> todoItemList = todoItemRepository.findAll();
        assertThat(todoItemList).hasSize(databaseSizeBeforeUpdate);
        TodoItem testTodoItem = todoItemList.get(todoItemList.size() - 1);
        assertThat(testTodoItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTodoItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTodoItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodoItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testTodoItem.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTodoItem() throws Exception {
        int databaseSizeBeforeUpdate = todoItemRepository.findAll().size();

        // Create the TodoItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTodoItemMockMvc.perform(put("/api/todo-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(todoItem)))
            .andExpect(status().isCreated());

        // Validate the TodoItem in the database
        List<TodoItem> todoItemList = todoItemRepository.findAll();
        assertThat(todoItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTodoItem() throws Exception {
        // Initialize the database
        todoItemService.save(todoItem);

        int databaseSizeBeforeDelete = todoItemRepository.findAll().size();

        // Get the todoItem
        restTodoItemMockMvc.perform(delete("/api/todo-items/{id}", todoItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TodoItem> todoItemList = todoItemRepository.findAll();
        assertThat(todoItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodoItem.class);
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        TodoItem todoItem2 = new TodoItem();
        todoItem2.setId(todoItem1.getId());
        assertThat(todoItem1).isEqualTo(todoItem2);
        todoItem2.setId(2L);
        assertThat(todoItem1).isNotEqualTo(todoItem2);
        todoItem1.setId(null);
        assertThat(todoItem1).isNotEqualTo(todoItem2);
    }
}
