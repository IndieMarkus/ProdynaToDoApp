package at.markus.lehr.prodynatodo.web.rest;

import at.markus.lehr.prodynatodo.ProdynaToDoApp;
import at.markus.lehr.prodynatodo.domain.ToDoEntry;
import at.markus.lehr.prodynatodo.repository.ToDoEntryRepository;
import at.markus.lehr.prodynatodo.service.ToDoEntryService;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;
import at.markus.lehr.prodynatodo.service.mapper.ToDoEntryMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ToDoEntryResource} REST controller.
 */
@SpringBootTest(classes = ProdynaToDoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ToDoEntryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    @Autowired
    private ToDoEntryRepository toDoEntryRepository;

    @Autowired
    private ToDoEntryMapper toDoEntryMapper;

    @Autowired
    private ToDoEntryService toDoEntryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToDoEntryMockMvc;

    private ToDoEntry toDoEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoEntry createEntity(EntityManager em) {
        ToDoEntry toDoEntry = new ToDoEntry()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .published(DEFAULT_PUBLISHED)
            .dueDate(DEFAULT_DUE_DATE)
            .done(DEFAULT_DONE);
        return toDoEntry;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoEntry createUpdatedEntity(EntityManager em) {
        ToDoEntry toDoEntry = new ToDoEntry()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .published(UPDATED_PUBLISHED)
            .dueDate(UPDATED_DUE_DATE)
            .done(UPDATED_DONE);
        return toDoEntry;
    }

    @BeforeEach
    public void initTest() {
        toDoEntry = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser
    public void createToDoEntry() throws Exception {
        int databaseSizeBeforeCreate = toDoEntryRepository.findAll().size();
        // Create the ToDoEntry
        ToDoEntryDTO toDoEntryDTO = toDoEntryMapper.toDto(toDoEntry);
        restToDoEntryMockMvc.perform(post("/api/to-do-entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDoEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the ToDoEntry in the database
        List<ToDoEntry> toDoEntryList = toDoEntryRepository.findAll();
        assertThat(toDoEntryList).hasSize(databaseSizeBeforeCreate + 1);
        ToDoEntry testToDoEntry = toDoEntryList.get(toDoEntryList.size() - 1);
        assertThat(testToDoEntry.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testToDoEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testToDoEntry.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testToDoEntry.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testToDoEntry.isDone()).isEqualTo(DEFAULT_DONE);
    }

    @Test
    @Transactional
    public void createToDoEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toDoEntryRepository.findAll().size();

        // Create the ToDoEntry with an existing ID
        toDoEntry.setId(1L);
        ToDoEntryDTO toDoEntryDTO = toDoEntryMapper.toDto(toDoEntry);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToDoEntryMockMvc.perform(post("/api/to-do-entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDoEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoEntry in the database
        List<ToDoEntry> toDoEntryList = toDoEntryRepository.findAll();
        assertThat(toDoEntryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = toDoEntryRepository.findAll().size();
        // set the field null
        toDoEntry.setTitle(null);

        // Create the ToDoEntry, which fails.
        ToDoEntryDTO toDoEntryDTO = toDoEntryMapper.toDto(toDoEntry);


        restToDoEntryMockMvc.perform(post("/api/to-do-entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDoEntryDTO)))
            .andExpect(status().isBadRequest());

        List<ToDoEntry> toDoEntryList = toDoEntryRepository.findAll();
        assertThat(toDoEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser
    public void getAllToDoEntries() throws Exception {
        // Initialize the database
        toDoEntryService.save(toDoEntry);

        // Get all the toDoEntryList
        restToDoEntryMockMvc.perform(get("/api/to-do-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));
    }

    @Test
    @Transactional
    public void getToDoEntry() throws Exception {
        // Initialize the database
        toDoEntryService.save(toDoEntry);

        // Get the toDoEntry
        restToDoEntryMockMvc.perform(get("/api/to-do-entries/{id}", toDoEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toDoEntry.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingToDoEntry() throws Exception {
        // Get the toDoEntry
        restToDoEntryMockMvc.perform(get("/api/to-do-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    public void updateToDoEntry() throws Exception {
        // Initialize the database
        toDoEntryService.save(toDoEntry);

        int databaseSizeBeforeUpdate = toDoEntryRepository.findAll().size();

        // Update the toDoEntry
        ToDoEntry updatedToDoEntry = toDoEntryRepository.findById(toDoEntry.getId()).get();
        // Disconnect from session so that the updates on updatedToDoEntry are not directly saved in db
        em.detach(updatedToDoEntry);
        updatedToDoEntry
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .published(UPDATED_PUBLISHED)
            .dueDate(UPDATED_DUE_DATE)
            .done(UPDATED_DONE);
        ToDoEntryDTO toDoEntryDTO = toDoEntryMapper.toDto(updatedToDoEntry);

        restToDoEntryMockMvc.perform(put("/api/to-do-entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDoEntryDTO)))
            .andExpect(status().isOk());

        // Validate the ToDoEntry in the database
        List<ToDoEntry> toDoEntryList = toDoEntryRepository.findAll();
        assertThat(toDoEntryList).hasSize(databaseSizeBeforeUpdate);
        ToDoEntry testToDoEntry = toDoEntryList.get(toDoEntryList.size() - 1);
        assertThat(testToDoEntry.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testToDoEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToDoEntry.isPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testToDoEntry.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testToDoEntry.isDone()).isEqualTo(UPDATED_DONE);
    }

    @Test
    @Transactional
    public void updateNonExistingToDoEntry() throws Exception {
        int databaseSizeBeforeUpdate = toDoEntryRepository.findAll().size();

        // Create the ToDoEntry
        ToDoEntryDTO toDoEntryDTO = toDoEntryMapper.toDto(toDoEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoEntryMockMvc.perform(put("/api/to-do-entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDoEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoEntry in the database
        List<ToDoEntry> toDoEntryList = toDoEntryRepository.findAll();
        assertThat(toDoEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser
    public void deleteToDoEntry() throws Exception {
        // Initialize the database
        toDoEntryService.save(toDoEntry);

        int databaseSizeBeforeDelete = toDoEntryRepository.findAll().size();

        // Delete the toDoEntry
        restToDoEntryMockMvc.perform(delete("/api/to-do-entries/{id}", toDoEntry.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToDoEntry> toDoEntryList = toDoEntryRepository.findAll();
        assertThat(toDoEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
