package at.markus.lehr.prodynatodo.repository;

import at.markus.lehr.prodynatodo.domain.ToDoEntry;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ToDoEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoEntryRepository extends JpaRepository<ToDoEntry, Long>, JpaSpecificationExecutor<ToDoEntry> {

    @Query("select toDoEntry from ToDoEntry toDoEntry where toDoEntry.creator.login = ?#{principal.username}")
    List<ToDoEntry> findByCreatorIsCurrentUser();
}
