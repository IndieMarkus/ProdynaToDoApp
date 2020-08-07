package at.markus.lehr.prodynatodo.service;

import at.markus.lehr.prodynatodo.domain.ToDoEntry;
import at.markus.lehr.prodynatodo.domain.User;
import at.markus.lehr.prodynatodo.repository.ToDoEntryRepository;
import at.markus.lehr.prodynatodo.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("generateData")
public class DataGenerator {
    private final int TODOS_TO_GENERATE = 1000;
    private final Long USER_ID = 3L;
    private final ToDoEntryRepository toDoEntryRepository;
    private final UserRepository userRepository;

    public DataGenerator(ToDoEntryRepository toDoEntryRepository, UserRepository userRepository) {
        this.toDoEntryRepository = toDoEntryRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void generateToDoEntries() {
        User admin = this.userRepository.findById(USER_ID).orElse(null);
        for (int i = 0; i < TODOS_TO_GENERATE; i++) {
            ToDoEntry entry = new ToDoEntry()
                .creator(admin)
                .title("TODO " + i)
                .description("Test Description is a little bit longer than the title");
            this.toDoEntryRepository.save(entry);
        }
    }
}
