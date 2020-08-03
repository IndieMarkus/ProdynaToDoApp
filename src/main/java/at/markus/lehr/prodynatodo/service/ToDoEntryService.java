package at.markus.lehr.prodynatodo.service;

import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link at.markus.lehr.prodynatodo.domain.ToDoEntry}.
 */
public interface ToDoEntryService {

    /**
     * Save a toDoEntry.
     *
     * @param toDoEntryDTO the entity to save.
     * @return the persisted entity.
     */
    ToDoEntryDTO save(ToDoEntryDTO toDoEntryDTO);


    /**
     * Check if current user is owner of entry with corresponding id.
     * @return true iff the id's entry's owner coincides with current user.
     */
    boolean allowedToModify(Long id);

    /**
     * Get all the toDoEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToDoEntryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" toDoEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToDoEntryDTO> findOne(Long id);

    /**
     * Delete the "id" toDoEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
