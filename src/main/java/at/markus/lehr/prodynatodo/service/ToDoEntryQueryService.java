package at.markus.lehr.prodynatodo.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import at.markus.lehr.prodynatodo.domain.ToDoEntry;
import at.markus.lehr.prodynatodo.domain.*; // for static metamodels
import at.markus.lehr.prodynatodo.repository.ToDoEntryRepository;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryCriteria;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;
import at.markus.lehr.prodynatodo.service.mapper.ToDoEntryMapper;

/**
 * Service for executing complex queries for {@link ToDoEntry} entities in the database.
 * The main input is a {@link ToDoEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ToDoEntryDTO} or a {@link Page} of {@link ToDoEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ToDoEntryQueryService extends QueryService<ToDoEntry> {

    private final Logger log = LoggerFactory.getLogger(ToDoEntryQueryService.class);

    private final ToDoEntryRepository toDoEntryRepository;

    private final ToDoEntryMapper toDoEntryMapper;

    public ToDoEntryQueryService(ToDoEntryRepository toDoEntryRepository, ToDoEntryMapper toDoEntryMapper) {
        this.toDoEntryRepository = toDoEntryRepository;
        this.toDoEntryMapper = toDoEntryMapper;
    }

    /**
     * Return a {@link List} of {@link ToDoEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ToDoEntryDTO> findByCriteria(ToDoEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ToDoEntry> specification = createSpecification(criteria);
        return toDoEntryMapper.toDto(toDoEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ToDoEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ToDoEntryDTO> findByCriteria(ToDoEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ToDoEntry> specification = createSpecification(criteria);
        return toDoEntryRepository.findAll(specification, page)
            .map(toDoEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ToDoEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ToDoEntry> specification = createSpecification(criteria);
        return toDoEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link ToDoEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ToDoEntry> createSpecification(ToDoEntryCriteria criteria) {
        Specification<ToDoEntry> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ToDoEntry_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ToDoEntry_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ToDoEntry_.description));
            }
            if (criteria.getPublished() != null) {
                specification = specification.and(buildSpecification(criteria.getPublished(), ToDoEntry_.published));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), ToDoEntry_.dueDate));
            }
            if (criteria.getDone() != null) {
                specification = specification.and(buildSpecification(criteria.getDone(), ToDoEntry_.done));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(ToDoEntry_.creator, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
