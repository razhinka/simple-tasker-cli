import com.engine.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private final Project mockProject = new Project(1, "mock project");
    private final User mockUser = new User(1, "mock user");

    @Test
    @DisplayName("Should mark a task done")
    void shouldMarkTaskDone() {
        Task testTask = new Task.Builder(0, "mock test", mockProject, mockUser).build();
        testTask = testTask.markDone();
        assertTrue(testTask.isDone());
    }

    @Test
    @DisplayName("Should return false for isDone")
    void isDoneReturnsFalse() {
        Task testTask = new Task.Builder(0, "mock test", mockProject, mockUser).build();
        assertFalse(testTask.isDone());
    }

    @Test
    @DisplayName("Should return true for isDone")
    void isDoneReturnsTrue() {
        Task testTask = new Task.Builder(0, "mock test", mockProject, mockUser).status(Status.DONE).build();
        assertTrue(testTask.isDone());
    }

    @Test
    @DisplayName("Has tag should return true")
    void hasTagReturnsTrue() {
        Tag tag = new Tag("test");
        Set<Tag> testTags = new HashSet<>();
        testTags.add(tag);
        Task testTask = new Task.Builder(0, "mock test", mockProject, mockUser)
                .tags(testTags).build();
        assertTrue(testTask.hasTag(tag));
    }

    @Test
    @DisplayName("Two independent tasks with same id should be equal")
    void twoTasksWithSameIdShouldBeEqual() {
        Task task1 = new Task.Builder(1, "title", mockProject, mockUser).build();
        Task task2 = new Task.Builder(1, "different title", mockProject, mockUser) // другое название
                .status(Status.DONE) // другой статус
                .build();
        assertEquals(task1, task2, "Should be equal because id matches");
    }

    @Test
    @DisplayName("Should return true for tasks with same id but different others parameters")
    void sameIdTasksReturnsTrue() {
        Task testTask = new Task.Builder(0, "mock test", mockProject, mockUser).build();
        Task testTask2 = new Task.Builder(0, "another mock test", mockProject, mockUser).build();
        assertEquals(testTask, testTask2);
    }

    @Test
    @DisplayName("Should return false for tasks with different ids")
    void differentIdTasksReturnsFalse() {
        Task testTask = new Task.Builder(0, "mock test", mockProject, mockUser).build();
        Task testTask2 = new Task.Builder(1, "mock test", mockProject, mockUser).build();
        assertNotEquals(testTask, testTask2);
    }

    @Test
    @DisplayName("hashCode should be consistent with equals")
    void hashCodeConsistentWithEquals() {
        Task task1 = new Task.Builder(1, "task", mockProject, mockUser).build();
        Task task2 = new Task.Builder(1, "task", mockProject, mockUser).build();
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    @DisplayName("Different ids should produce different hashCodes (not required but good practice)")
    void differentIdsDifferentHashCodes() {
        Task task1 = new Task.Builder(1, "task", mockProject, mockUser).build();
        Task task2 = new Task.Builder(2, "task", mockProject, mockUser).build();
        assertNotEquals(task1.hashCode(), task2.hashCode());
    }
}
