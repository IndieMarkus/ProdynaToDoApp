package at.markus.lehr.prodynatodo.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ToDoEntryMapperTest {

    private ToDoEntryMapper toDoEntryMapper;

    @BeforeEach
    public void setUp() {
        toDoEntryMapper = new ToDoEntryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(toDoEntryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(toDoEntryMapper.fromId(null)).isNull();
    }
}
