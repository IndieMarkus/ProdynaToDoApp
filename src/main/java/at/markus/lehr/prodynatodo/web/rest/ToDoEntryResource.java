package at.markus.lehr.prodynatodo.web.rest;

import at.markus.lehr.prodynatodo.service.ToDoEntryService;
import at.markus.lehr.prodynatodo.web.rest.errors.BadRequestAlertException;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link at.markus.lehr.prodynatodo.domain.ToDoEntry}.
 */
@RestController
@RequestMapping("/api")
public class ToDoEntryResource {

    private final Logger log = LoggerFactory.getLogger(ToDoEntryResource.class);

    private static final String ENTITY_NAME = "toDoEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToDoEntryService toDoEntryService;

    public ToDoEntryResource(ToDoEntryService toDoEntryService) {
        this.toDoEntryService = toDoEntryService;
    }

    /**
     * {@code POST  /to-do-entries} : Create a new toDoEntry.
     *
     * @param toDoEntryDTO the toDoEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toDoEntryDTO, or with status {@code 400 (Bad Request)} if the toDoEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-do-entries")
    public ResponseEntity<ToDoEntryDTO> createToDoEntry(@Valid @RequestBody ToDoEntryDTO toDoEntryDTO) throws URISyntaxException {
        log.debug("REST request to save ToDoEntry : {}", toDoEntryDTO);
        if (toDoEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new toDoEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (toDoEntryDTO.getCreatorId() != null) {
            throw new BadRequestAlertException("The creator ID must not be manually set", ENTITY_NAME, "idexists");
        }
        ToDoEntryDTO result = toDoEntryService.save(toDoEntryDTO);
        return ResponseEntity.created(new URI("/api/to-do-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /to-do-entries} : Updates an existing toDoEntry.
     *
     * @param toDoEntryDTO the toDoEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoEntryDTO,
     * or with status {@code 400 (Bad Request)} if the toDoEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toDoEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-do-entries")
    public ResponseEntity<ToDoEntryDTO> updateToDoEntry(@Valid @RequestBody ToDoEntryDTO toDoEntryDTO) throws URISyntaxException {
        log.debug("REST request to update ToDoEntry : {}", toDoEntryDTO);
        if (toDoEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!toDoEntryService.allowedToModify(toDoEntryDTO.getId())) {
            throw new BadRequestAlertException("The current user is not the owner of the modified todo entry", ENTITY_NAME, "wrongcreator");
        }
        ToDoEntryDTO result = toDoEntryService.save(toDoEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toDoEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /to-do-entries} : get all the toDoEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toDoEntries in body.
     */
    @GetMapping("/to-do-entries")
    public ResponseEntity<List<ToDoEntryDTO>> getAllToDoEntries(Pageable pageable) {
        log.debug("REST request to get a page of ToDoEntries");
        Page<ToDoEntryDTO> page = toDoEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /to-do-entries/:id} : get the "id" toDoEntry.
     *
     * @param id the id of the toDoEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toDoEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-do-entries/{id}")
    public ResponseEntity<ToDoEntryDTO> getToDoEntry(@PathVariable Long id) {
        log.debug("REST request to get ToDoEntry : {}", id);
        Optional<ToDoEntryDTO> toDoEntryDTO = toDoEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toDoEntryDTO);
    }

    /**
     * {@code DELETE  /to-do-entries/:id} : delete the "id" toDoEntry.
     *
     * @param id the id of the toDoEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-do-entries/{id}")
    public ResponseEntity<Void> deleteToDoEntry(@PathVariable Long id) {
        log.debug("REST request to delete ToDoEntry : {}", id);
        if (!toDoEntryService.allowedToModify(id)) {
            throw new BadRequestAlertException("The current user is not the owner of the modified todo entry", ENTITY_NAME, "wrongcreator");
        }
        toDoEntryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
