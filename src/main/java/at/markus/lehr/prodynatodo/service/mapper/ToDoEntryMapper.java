package at.markus.lehr.prodynatodo.service.mapper;


import at.markus.lehr.prodynatodo.domain.*;
import at.markus.lehr.prodynatodo.service.dto.ToDoEntryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToDoEntry} and its DTO {@link ToDoEntryDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ToDoEntryMapper extends EntityMapper<ToDoEntryDTO, ToDoEntry> {

    @Mapping(source = "creator.id", target = "creatorId")
    ToDoEntryDTO toDto(ToDoEntry toDoEntry);

    @Mapping(source = "creatorId", target = "creator")
    ToDoEntry toEntity(ToDoEntryDTO toDoEntryDTO);

    default ToDoEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        ToDoEntry toDoEntry = new ToDoEntry();
        toDoEntry.setId(id);
        return toDoEntry;
    }
}
