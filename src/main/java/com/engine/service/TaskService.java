package com.engine.service;

import com.engine.dto.TaskRequestDto;
import com.engine.model.*;
import com.engine.repository.ProjectRepository;
import com.engine.repository.TaskRepository;
import com.engine.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " not found"));
    }

    @Transactional
    public Task createTaskFromDto(TaskRequestDto dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + dto.projectId()));

        User assignee = userRepository.findById(dto.assigneeId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.assigneeId()));

        Task task = new Task.Builder(null, dto.title(), project, assignee)
                .description(dto.description())
                .priority(dto.priority() != null ? dto.priority() : Priority.LOW)
                .status(Status.NOT_STARTED)
                .deadline(dto.deadline())
                .build();

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTaskFromDto(Long id, TaskRequestDto dto) {
        Task existingTask = getTaskById(id);

        if (dto.status() != null && existingTask.getStatus() != dto.status()) {
            validateStatusTransition(existingTask.getStatus(), dto.status());
            existingTask.setStatus(dto.status());
        }

        if (dto.title() != null) {
            existingTask.setTitle(dto.title());
        }
        if (dto.description() != null) {
            existingTask.setDescription(dto.description());
        }
        if (dto.priority() != null) {
            existingTask.setPriority(dto.priority());
        }
        existingTask.setDeadline(dto.deadline());

        if (dto.projectId() != null) {
            Long currentProjectId = existingTask.getProject() != null ? existingTask.getProject().getId() : null;
            if (!dto.projectId().equals(currentProjectId)) {
                Project project = projectRepository.findById(dto.projectId())
                        .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + dto.projectId()));
                existingTask.setProject(project);
            }
        }

        if (dto.assigneeId() != null) {
            Long currentAssigneeId = existingTask.getAssignee() != null ? existingTask.getAssignee().getId() : null;
            if (!dto.assigneeId().equals(currentAssigneeId)) {
                User assignee = userRepository.findById(dto.assigneeId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.assigneeId()));
                existingTask.setAssignee(assignee);
            }
        }

        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    private void validateStatusTransition(Status currentStatus, Status newStatus) {
        if (currentStatus == Status.NOT_STARTED && newStatus == Status.DONE) {
            throw new IllegalStateException(
                    "Cannot complete a task directly from NOT_STARTED. Move it to IN_PROGRESS first!"
            );
        }
    }
}