package at.markus.lehr.prodynatodo.service.impl;

import at.markus.lehr.prodynatodo.domain.ToDoEntry;
import at.markus.lehr.prodynatodo.domain.User;
import at.markus.lehr.prodynatodo.repository.ToDoEntryRepository;
import at.markus.lehr.prodynatodo.repository.UserRepository;
import at.markus.lehr.prodynatodo.security.SecurityUtils;
import at.markus.lehr.prodynatodo.service.ToDoEntryService;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;
import at.markus.lehr.prodynatodo.service.mapper.ToDoEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ToDoEntry}.
 */
@Service
@Transactional
public class ToDoEntryServiceImpl implements ToDoEntryService {

    private final Logger log = LoggerFactory.getLogger(ToDoEntryServiceImpl.class);

    private final ToDoEntryRepository toDoEntryRepository;

    private final UserRepository userRepository;

    private final ToDoEntryMapper toDoEntryMapper;

    public ToDoEntryServiceImpl(ToDoEntryRepository toDoEntryRepository, UserRepository userRepository, ToDoEntryMapper toDoEntryMapper) {
        this.toDoEntryRepository = toDoEntryRepository;
        this.userRepository = userRepository;
        this.toDoEntryMapper = toDoEntryMapper;
    }

    @Override
    public ToDoEntryDTO save(ToDoEntryDTO toDoEntryDTO) {
        log.debug("Request to save ToDoEntry : {}", toDoEntryDTO);
        ToDoEntry toDoEntry = toDoEntryMapper.toEntity(toDoEntryDTO);
        setCurrentUser(toDoEntry);
        toDoEntry = toDoEntryRepository.save(toDoEntry);
        return toDoEntryMapper.toDto(toDoEntry);
    }

    @Override
    public ToDoEntryDTO save(ToDoEntry toDoEntry) {
        log.debug("Request to save ToDoEntry : {}", toDoEntry);
        setCurrentUser(toDoEntry);
        toDoEntry = toDoEntryRepository.save(toDoEntry);
        return toDoEntryMapper.toDto(toDoEntry);
    }

    @Override
    public boolean allowedToModify(Long id) {
        ToDoEntry entry = this.toDoEntryRepository.getOne(id);
        Long currentId = getCurrentUser().getId();
        return entry.getCreator().getId().equals(currentId);
    }

    private User getCurrentUser() {
        try {
            return this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
        } catch (NoSuchElementException ex) {
            String reason = "The currently logged in user object could not be extracted from the db";
            log.error(reason);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason, ex);
        }
    }

    private void setCurrentUser(ToDoEntry entry) {
        entry.setCreator(getCurrentUser());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToDoEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ToDoEntries");
        return toDoEntryRepository.findAllByPublishedIsTrueOrCreatorEquals(pageable, getCurrentUser())
            .map(toDoEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToDoEntryDTO> findCurrent(Pageable pageable) {
        log.debug("Request to get all ToDoEntries");
        return toDoEntryRepository.findAllByPublishedIsTrueOrCreatorEquals(pageable, getCurrentUser())
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
