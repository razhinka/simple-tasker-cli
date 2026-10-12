package com.engine.server;

import com.engine.model.Task;
import com.engine.repository.SQLTaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class SimpleHttpServer {
    private final int port;
    private final ExecutorService threadPool;
    private final SQLTaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    public SimpleHttpServer(int port, ExecutorService threadPool, SQLTaskRepository taskRepository) {
        this.port = port;
        this.threadPool = threadPool;
        this.taskRepository = taskRepository;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleRequest(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
        }
    }

    private void handleRequest(Socket socket) {
        try (Socket clientSocket = socket;
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            HttpRequest request = HttpParser.parse(clientSocket.getInputStream());
            if (request == null) {
                return;
            }
            HttpResponse response = route(request);
            writer.write(response.toRawHttp());
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error processing request: " + e.getMessage());
        }
    }

    private HttpResponse route(HttpRequest request) {
        String method = request.method().toUpperCase();
        String path = request.path();

        if ("GET".equals(method) && "/tasks".equals(path)) {
            return handleGetTasks();
        }
        if ("POST".equals(method) && "/tasks".equals(path)) {
            return handleCreateTask(request);
        }

        if (path.startsWith("/tasks/")) {
            try {
                long id = Long.parseLong(path.substring("/tasks/".length()));

                return switch (method) {
                    case "GET" -> handleGetTaskById(id);
                    case "PUT" -> handleUpdateTask(id, request);
                    case "DELETE" -> handleDeleteTask(id);
                    default -> HttpResponse.badRequest("Method not allowed");
                };
            } catch (NumberFormatException e) {
                return HttpResponse.badRequest("Invalid ID format: " + path);
            }
        }
        return HttpResponse.notFound("Not Found");
    }

    private HttpResponse handleGetTasks() {
        List<Task> tasks = taskRepository.findAll();
        try {
            String json = objectMapper.writeValueAsString(tasks);
            return HttpResponse.ok(json);
        } catch (JsonProcessingException e) {
            return HttpResponse.internalServerError(e.getMessage());
        }
    }

    private HttpResponse handleGetTaskById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return HttpResponse.notFound("Task not found");
        }
        try {
            String json = objectMapper.writeValueAsString(task.get());
            return HttpResponse.ok(json);
        } catch (JsonProcessingException e) {
            return HttpResponse.internalServerError(e.getMessage());
        }
    }

    private HttpResponse handleCreateTask(HttpRequest request) {
        try {
            Task task = objectMapper.readValue(request.body(), Task.class);
            if (task.getId() != 0) {
                task = new Task.Builder(task).id(0).build();
            }
            taskRepository.save(task);
            return HttpResponse.created("{\"status\": \"created\"}");
        } catch (JsonProcessingException e) {
            return HttpResponse.badRequest("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            return HttpResponse.internalServerError("Database error: " + e.getMessage());
        }
    }

    private HttpResponse handleDeleteTask(long id) {
        try {
            taskRepository.deleteById(id);
            return HttpResponse.ok("{\"status\": \"deleted\"}");
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound("Task not found");
        } catch (Exception e) {
            return HttpResponse.internalServerError(e.getMessage());
        }
    }

    private HttpResponse handleUpdateTask(long id, HttpRequest request) {
        try {
            Task updatedTask = objectMapper.readValue(request.body(), Task.class);
            Task taskToSave = new Task.Builder(updatedTask).id(id).build();
            taskRepository.save(taskToSave);
            return HttpResponse.ok("{\"status\": \"updated\"}");
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound("Task not found");
        } catch (JsonProcessingException e) {
            return HttpResponse.badRequest("Invalid JSON");
        } catch (Exception e) {
            return HttpResponse.internalServerError("e.getMessage()");
        }
    }
}
