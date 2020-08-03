package at.markus.lehr.prodynatodo.repository;

import at.markus.lehr.prodynatodo.domain.ToDoEntry;

import at.markus.lehr.prodynatodo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ToDoEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoEntryRepository extends JpaRepository<ToDoEntry, Long> {
    Page<ToDoEntry> findAllByPublishedIsTrueOrCreatorEquals(Pageable pageable, User creator);

    Page<ToDoEntry> findAllByPublishedIsTrueAndDoneIsFalseOrCreatorEqualsAndDoneIsFalse(Pageable pageable, User creator);
}
