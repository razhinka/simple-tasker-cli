import com.engine.model.*;
import com.engine.services.FileTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileTaskRepositoryTest {

    @TempDir
    Path tempDir;

    private File testFile;
    private FileTaskRepository repository;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        testFile = tempDir.resolve("tasks.json").toFile();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        repository = new FileTaskRepository(testFile, mapper);
    }

    @Test
    @DisplayName("Should save and load a task")
    void shouldSaveAndLoadTask() {
        // Given
        Project project = new Project(1, "Test Project");
        User user = new User(1, "testuser");
        Task original = new Task.Builder(0, "Learn JUnit", project, user)
                .deadline(LocalDateTime.now().plusDays(1))
                .priority(Priority.HIGH)
                .build();

        // When
        repository.save(original);
        List<Task> tasks = repository.findAll();

        // Then
        assertEquals(1, tasks.size());
        Task loaded = tasks.getFirst();
        assertEquals(original.getTitle(), loaded.getTitle());
        assertEquals(original.getDeadline(), loaded.getDeadline());
        assertEquals(original.getPriority(), loaded.getPriority());
        assertNotEquals(0, loaded.getId());
    }

    @Test
    @DisplayName("Should load existing tasks from file on startup")
    void shouldLoadFromFile() {
        // Given
        Task task1 = new Task.Builder(0, "Task 1", new Project(1, "p1"), new User(1, "u1")).build();
        Task task2 = new Task.Builder(0, "Task 2", new Project(2, "p2"), new User(2, "u2")).build();
        repository.save(task1);
        repository.save(task2);
        long savedId1 = repository.findAll().getFirst().getId();

        //When
        FileTaskRepository newRepo = new FileTaskRepository(testFile, mapper);

        // Then
        List<Task> tasks = newRepo.findAll();
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getId() == savedId1));
    }

    @Test
    @DisplayName("Should delete a task by id")
    void shouldDeleteTask() {
        // Given
        Task task = new Task.Builder(0, "To delete", new Project(1, "p"), new User(1, "u")).build();
        repository.save(task);
        long id = repository.findAll().getFirst().getId();

        // When
        repository.deleteById(id);

        // Then
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Should handle non-existent file gracefully")
    void shouldHandleMissingFile() {
        // Given
        File missingFile = tempDir.resolve("missing.json").toFile();
        //When
        FileTaskRepository repo = new FileTaskRepository(missingFile, mapper);

        // Then
        assertTrue(repo.findAll().isEmpty());
        repo.save(new Task.Builder(0, "first", new Project(1, "p"), new User(1, "u")).build());
        assertTrue(missingFile.exists());
    }
}