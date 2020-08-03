package at.markus.lehr.prodynatodo.service.impl;

import at.markus.lehr.prodynatodo.service.ToDoEntryService;
import at.markus.lehr.prodynatodo.domain.ToDoEntry;
import at.markus.lehr.prodynatodo.repository.ToDoEntryRepository;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;
import at.markus.lehr.prodynatodo.service.mapper.ToDoEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ToDoEntry}.
 */
@Service
@Transactional
public class ToDoEntryServiceImpl implements ToDoEntryService {

    private final Logger log = LoggerFactory.getLogger(ToDoEntryServiceImpl.class);

    private final ToDoEntryRepository toDoEntryRepository;

    private final ToDoEntryMapper toDoEntryMapper;

    public ToDoEntryServiceImpl(ToDoEntryRepository toDoEntryRepository, ToDoEntryMapper toDoEntryMapper) {
        this.toDoEntryRepository = toDoEntryRepository;
        this.toDoEntryMapper = toDoEntryMapper;
    }

    @Override
    public ToDoEntryDTO save(ToDoEntryDTO toDoEntryDTO) {
        log.debug("Request to save ToDoEntry : {}", toDoEntryDTO);
        ToDoEntry toDoEntry = toDoEntryMapper.toEntity(toDoEntryDTO);
        toDoEntry = toDoEntryRepository.save(toDoEntry);
        return toDoEntryMapper.toDto(toDoEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToDoEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ToDoEntries");
        return toDoEntryRepository.findAll(pageable)
            .map(toDoEntryMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ToDoEntryDTO> findOne(Long id) {
        log.debug("Request to get ToDoEntry : {}", id);
        return toDoEntryRepository.findById(id)
            .map(toDoEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToDoEntry : {}", id);
        toDoEntryRepository.deleteById(id);
    }
}
