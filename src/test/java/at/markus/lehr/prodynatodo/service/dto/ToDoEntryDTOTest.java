package at.markus.lehr.prodynatodo.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import at.markus.lehr.prodynatodo.web.rest.TestUtil;

public class ToDoEntryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoEntryDTO.class);
        ToDoEntryDTO toDoEntryDTO1 = new ToDoEntryDTO();
        toDoEntryDTO1.setId(1L);
        ToDoEntryDTO toDoEntryDTO2 = new ToDoEntryDTO();
        assertThat(toDoEntryDTO1).isNotEqualTo(toDoEntryDTO2);
        toDoEntryDTO2.setId(toDoEntryDTO1.getId());
        assertThat(toDoEntryDTO1).isEqualTo(toDoEntryDTO2);
        toDoEntryDTO2.setId(2L);
        assertThat(toDoEntryDTO1).isNotEqualTo(toDoEntryDTO2);
        toDoEntryDTO1.setId(null);
        assertThat(toDoEntryDTO1).isNotEqualTo(toDoEntryDTO2);
    }
}
