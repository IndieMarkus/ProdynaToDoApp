package at.markus.lehr.prodynatodo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import at.markus.lehr.prodynatodo.web.rest.TestUtil;

public class ToDoEntryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoEntry.class);
        ToDoEntry toDoEntry1 = new ToDoEntry();
        toDoEntry1.setId(1L);
        ToDoEntry toDoEntry2 = new ToDoEntry();
        toDoEntry2.setId(toDoEntry1.getId());
        assertThat(toDoEntry1).isEqualTo(toDoEntry2);
        toDoEntry2.setId(2L);
        assertThat(toDoEntry1).isNotEqualTo(toDoEntry2);
        toDoEntry1.setId(null);
        assertThat(toDoEntry1).isNotEqualTo(toDoEntry2);
    }
}
