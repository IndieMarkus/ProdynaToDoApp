package at.markus.lehr.prodynatodo.web.rest;

import at.markus.lehr.prodynatodo.ProdynaToDoApp;
import at.markus.lehr.prodynatodo.domain.ToDoEntry;
import at.markus.lehr.prodynatodo.domain.User;
import at.markus.lehr.prodynatodo.repository.ToDoEntryRepository;
import at.markus.lehr.prodynatodo.service.ToDoEntryService;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;
import at.markus.lehr.prodynatodo.service.mapper.ToDoEntryMapper;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryCriteria;
import at.markus.lehr.prodynatodo.service.ToDoEntryQueryService;

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
    private ToDoEntryQueryService toDoEntryQueryService;

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
    public void getAllToDoEntries() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

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
        toDoEntryRepository.saveAndFlush(toDoEntry);

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
    public void getToDoEntriesByIdFiltering() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        Long id = toDoEntry.getId();

        defaultToDoEntryShouldBeFound("id.equals=" + id);
        defaultToDoEntryShouldNotBeFound("id.notEquals=" + id);

        defaultToDoEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultToDoEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultToDoEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultToDoEntryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllToDoEntriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where title equals to DEFAULT_TITLE
        defaultToDoEntryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the toDoEntryList where title equals to UPDATED_TITLE
        defaultToDoEntryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where title not equals to DEFAULT_TITLE
        defaultToDoEntryShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the toDoEntryList where title not equals to UPDATED_TITLE
        defaultToDoEntryShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultToDoEntryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the toDoEntryList where title equals to UPDATED_TITLE
        defaultToDoEntryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where title is not null
        defaultToDoEntryShouldBeFound("title.specified=true");

        // Get all the toDoEntryList where title is null
        defaultToDoEntryShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllToDoEntriesByTitleContainsSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where title contains DEFAULT_TITLE
        defaultToDoEntryShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the toDoEntryList where title contains UPDATED_TITLE
        defaultToDoEntryShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where title does not contain DEFAULT_TITLE
        defaultToDoEntryShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the toDoEntryList where title does not contain UPDATED_TITLE
        defaultToDoEntryShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllToDoEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where description equals to DEFAULT_DESCRIPTION
        defaultToDoEntryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the toDoEntryList where description equals to UPDATED_DESCRIPTION
        defaultToDoEntryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where description not equals to DEFAULT_DESCRIPTION
        defaultToDoEntryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the toDoEntryList where description not equals to UPDATED_DESCRIPTION
        defaultToDoEntryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultToDoEntryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the toDoEntryList where description equals to UPDATED_DESCRIPTION
        defaultToDoEntryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where description is not null
        defaultToDoEntryShouldBeFound("description.specified=true");

        // Get all the toDoEntryList where description is null
        defaultToDoEntryShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllToDoEntriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where description contains DEFAULT_DESCRIPTION
        defaultToDoEntryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the toDoEntryList where description contains UPDATED_DESCRIPTION
        defaultToDoEntryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where description does not contain DEFAULT_DESCRIPTION
        defaultToDoEntryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the toDoEntryList where description does not contain UPDATED_DESCRIPTION
        defaultToDoEntryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllToDoEntriesByPublishedIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where published equals to DEFAULT_PUBLISHED
        defaultToDoEntryShouldBeFound("published.equals=" + DEFAULT_PUBLISHED);

        // Get all the toDoEntryList where published equals to UPDATED_PUBLISHED
        defaultToDoEntryShouldNotBeFound("published.equals=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByPublishedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where published not equals to DEFAULT_PUBLISHED
        defaultToDoEntryShouldNotBeFound("published.notEquals=" + DEFAULT_PUBLISHED);

        // Get all the toDoEntryList where published not equals to UPDATED_PUBLISHED
        defaultToDoEntryShouldBeFound("published.notEquals=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByPublishedIsInShouldWork() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where published in DEFAULT_PUBLISHED or UPDATED_PUBLISHED
        defaultToDoEntryShouldBeFound("published.in=" + DEFAULT_PUBLISHED + "," + UPDATED_PUBLISHED);

        // Get all the toDoEntryList where published equals to UPDATED_PUBLISHED
        defaultToDoEntryShouldNotBeFound("published.in=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByPublishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where published is not null
        defaultToDoEntryShouldBeFound("published.specified=true");

        // Get all the toDoEntryList where published is null
        defaultToDoEntryShouldNotBeFound("published.specified=false");
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where dueDate equals to DEFAULT_DUE_DATE
        defaultToDoEntryShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the toDoEntryList where dueDate equals to UPDATED_DUE_DATE
        defaultToDoEntryShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where dueDate not equals to DEFAULT_DUE_DATE
        defaultToDoEntryShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the toDoEntryList where dueDate not equals to UPDATED_DUE_DATE
        defaultToDoEntryShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultToDoEntryShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the toDoEntryList where dueDate equals to UPDATED_DUE_DATE
        defaultToDoEntryShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where dueDate is not null
        defaultToDoEntryShouldBeFound("dueDate.specified=true");

        // Get all the toDoEntryList where dueDate is null
        defaultToDoEntryShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDoneIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where done equals to DEFAULT_DONE
        defaultToDoEntryShouldBeFound("done.equals=" + DEFAULT_DONE);

        // Get all the toDoEntryList where done equals to UPDATED_DONE
        defaultToDoEntryShouldNotBeFound("done.equals=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where done not equals to DEFAULT_DONE
        defaultToDoEntryShouldNotBeFound("done.notEquals=" + DEFAULT_DONE);

        // Get all the toDoEntryList where done not equals to UPDATED_DONE
        defaultToDoEntryShouldBeFound("done.notEquals=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDoneIsInShouldWork() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where done in DEFAULT_DONE or UPDATED_DONE
        defaultToDoEntryShouldBeFound("done.in=" + DEFAULT_DONE + "," + UPDATED_DONE);

        // Get all the toDoEntryList where done equals to UPDATED_DONE
        defaultToDoEntryShouldNotBeFound("done.in=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByDoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

        // Get all the toDoEntryList where done is not null
        defaultToDoEntryShouldBeFound("done.specified=true");

        // Get all the toDoEntryList where done is null
        defaultToDoEntryShouldNotBeFound("done.specified=false");
    }

    @Test
    @Transactional
    public void getAllToDoEntriesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);
        User creator = UserResourceIT.createEntity(em);
        em.persist(creator);
        em.flush();
        toDoEntry.setCreator(creator);
        toDoEntryRepository.saveAndFlush(toDoEntry);
        Long creatorId = creator.getId();

        // Get all the toDoEntryList where creator equals to creatorId
        defaultToDoEntryShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the toDoEntryList where creator equals to creatorId + 1
        defaultToDoEntryShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultToDoEntryShouldBeFound(String filter) throws Exception {
        restToDoEntryMockMvc.perform(get("/api/to-do-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));

        // Check, that the count call also returns 1
        restToDoEntryMockMvc.perform(get("/api/to-do-entries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultToDoEntryShouldNotBeFound(String filter) throws Exception {
        restToDoEntryMockMvc.perform(get("/api/to-do-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restToDoEntryMockMvc.perform(get("/api/to-do-entries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
    public void updateToDoEntry() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

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
    public void deleteToDoEntry() throws Exception {
        // Initialize the database
        toDoEntryRepository.saveAndFlush(toDoEntry);

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
