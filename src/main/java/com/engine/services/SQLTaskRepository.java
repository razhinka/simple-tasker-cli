package com.engine.services;

import com.engine.model.*;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SQLTaskRepository implements TaskRepository {
    private final DataSource dataSource;

    public SQLTaskRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void markDone(Task task) {
        String sql = "UPDATE tasks SET status = 'DONE' WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, task.getId());
            pstmt.executeUpdate();
            System.out.println(task.getId() + " marked as DONE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Task> findByTitle(String title) {
        return findOne("t.title = ?", title);
    }

    @Override
    public void deleteByTitle(String title) {
        String sql = "DELETE FROM tasks WHERE title = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error by deleting tasks", e);
        }
    }

    @Override
    public Task save(Task o) {
        if (o.getId() == 0) {
            return insert(o);
        } else {
            return update(o);
        }
    }

    private Task update(Task o) {
        String updateTaskSql = """
                UPDATE tasks
                SET title = ?,
                description = ?,
                status = ?,
                deadline = ?,
                project_id = ?,
                assignee_id = ?
                WHERE id = ?
                """;
        String deleteTagSql = "DELETE FROM task_tags WHERE task_id = ?";
        String insertTagSql = """
                INSERT INTO task_tags (task_id, tag_id)
                SELECT ?, id FROM tags WHERE tag = ?
                """;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pstmt = conn.prepareStatement(updateTaskSql)) {
                    pstmt.setString(1, o.getTitle());
                    pstmt.setString(2, o.getDescription());
                    pstmt.setString(3, o.getStatus().toString());
                    pstmt.setString(4, o.getPriority().toString());
                    pstmt.setObject(5, o.getDeadline().orElse(null));
                    pstmt.setLong(6, o.getProject().id());
                    pstmt.setLong(7, o.getAssignee().id());
                    pstmt.setLong(8, o.getId());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    conn.rollback();
                    throw new RuntimeException(e);
                }
                // Deleting old tags
                try (PreparedStatement pstmt = conn.prepareStatement(deleteTagSql)) {
                    pstmt.setLong(1, o.getId());
                }
                // Inserting new tags
                if (!o.getTags().isEmpty()) {
                    try (PreparedStatement pstmt = conn.prepareStatement(insertTagSql)) {
                        for (Tag t : o.getTags()) {
                            pstmt.setLong(1, o.getId());
                            pstmt.setString(2, t.name());
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();
                    }
                }
                conn.commit();
                return o;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Task insert(Task o) {
        String insertTaskSql = """
            INSERT INTO tasks (title, description, status, priority, deadline, project_id, assignee_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        String insertTagSql = """
            INSERT INTO task_tags (task_id, tag_id)
            SELECT ?, id FROM tags WHERE tag = ?
            """;

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                long generatedTaskId;

                try (PreparedStatement pstmt = conn.prepareStatement(insertTaskSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, o.getTitle());
                    pstmt.setString(2, o.getDescription());
                    pstmt.setString(3, o.getStatus().name());
                    pstmt.setString(4, o.getPriority().name());
                    pstmt.setObject(5, o.getDeadline().orElse(null));
                    pstmt.setLong(6, o.getProject().id());
                    pstmt.setLong(7, o.getAssignee().id());

                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            generatedTaskId = rs.getLong(1);
                        } else {
                            throw new SQLException("Создание задачи не удалось, ID не получен.");
                        }
                    }
                }

                if (!o.getTags().isEmpty()) {
                    try (PreparedStatement pstmt = conn.prepareStatement(insertTagSql)) {
                        for (Tag t : o.getTags()) {
                            pstmt.setLong(1, generatedTaskId);
                            pstmt.setString(2, t.name());
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();
                    }
                }
                conn.commit();
                return new Task.Builder(o).id(generatedTaskId).build();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при создании задачи", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Task o) {
        if (o != null) {
            deleteById(o.getId());
        }
    }

    @Override
    public List<Task> findAll() {
        String sql = """
                SELECT t.id AS task_id, t.title AS task_title, t.description, t.status, t.priority, t.deadline,
                       p.id AS project_id, p.title AS project_title,
                       u.id AS assignee_id, u.username AS username
                FROM tasks t
                JOIN projects p ON p.id = t.project_id
                JOIN users u ON u.id = t.assignee_id
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Task> tasks = new ArrayList<>();
                while (rs.next()) {
                    long taskId = rs.getLong("task_id");
                    Task.Builder builder = mapRowToTaskBuilder(rs);
                    builder.tags(findTagsByTaskId(taskId, conn));
                    builder.comments(findCommentsByTaskId(taskId, conn));
                    tasks.add(builder.build());
                }
                return tasks;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Task> findById(long id) {
        return findOne("t.id = ?", id);
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error by deleting task", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT count(*) FROM tasks";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Task.Builder mapRowToTaskBuilder(ResultSet rs) throws SQLException {
        Project project = new Project(rs.getLong("project_id"), rs.getString("project_title"));
        User assignee = new User(rs.getLong("assignee_id"), rs.getString("username"));

        return new Task.Builder(
                rs.getLong("task_id"),
                rs.getString("task_title"),
                project,
                assignee
        )
                .description(rs.getString("description")) // Не забываем про описание
                .status(Status.valueOf(rs.getString("status")))
                .priority(Priority.valueOf(rs.getString("priority")))
                .deadline(rs.getObject("deadline", LocalDateTime.class));
    }

    private Optional<Task> findOne(String condition, Object param) {
        String sql = """
                SELECT t.id AS task_id, t.title AS task_title, t.description, t.status, t.priority, t.deadline,
                       p.id AS project_id, p.title AS project_title,
                       u.id AS assignee_id, u.username AS username
                FROM tasks t
                JOIN projects p ON p.id = t.project_id
                JOIN users u ON u.id = t.assignee_id
                WHERE %s
                """.formatted(condition);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, param);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long taskId = rs.getLong("task_id");
                    Task.Builder builder = mapRowToTaskBuilder(rs);
                    builder.tags(findTagsByTaskId(taskId, conn));
                    builder.comments(findCommentsByTaskId(taskId, conn));

                    return Optional.of(builder.build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса: " + sql, e);
        }
        return Optional.empty();
    }

    private List<Comment> findCommentsByTaskId(long taskId, Connection conn) {
        String sql = "SELECT comments.*, users.username FROM comments" +
                " JOIN users ON users.id = comments.author_id " +
                "WHERE task_id = ? ORDER BY created";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, taskId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Comment> comments = new LinkedList<>();
                while (rs.next()) {
                    User user = new User(rs.getLong("author_id"), rs.getString("username"));
                    comments.add(new Comment(
                                    rs.getLong("id"),
                                    user,
                                    rs.getString("text"),
                                    rs.getObject("created", LocalDateTime.class)
                            )
                    );
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Tag> findTagsByTaskId(long taskId, Connection conn) {
        String sql = "SELECT tags.tag FROM task_tags" +
                " JOIN tags ON task_tags.tag_id = tags.id" +
                " WHERE task_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                pstmt.setLong(1, taskId);
                Set<Tag> tags = new HashSet<>();
                while (rs.next()) {
                    tags.add(new Tag(rs.getString("tag")));
                }
                return tags;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
