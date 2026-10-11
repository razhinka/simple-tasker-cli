package com.engine;

import com.engine.config.DatabasePool;
import com.engine.repository.SQLTaskRepository;
import com.engine.server.SimpleHttpServer;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = DatabasePool.getDataSource();
        SQLTaskRepository taskRepository = new SQLTaskRepository(dataSource);
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            SimpleHttpServer server = new SimpleHttpServer(8080, executor, taskRepository);
            server.start();
        }
    }
}
