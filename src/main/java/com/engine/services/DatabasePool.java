package com.engine.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabasePool {
    private static final HikariDataSource pool;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("lakoz900");

        config.setMaximumPoolSize(10); // Максимальное количество открытых соединений к БД
        config.setMinimumIdle(2);      // Сколько соединений держать открытыми даже при простое
        config.setConnectionTimeout(30000); // Тайм-аут ожидания свободного соединения (30 сек)
        config.setIdleTimeout(600000);      // Время жизни простаивающего соединения (10 мин)

        pool = new HikariDataSource(config);
    }

    private DatabasePool() {}

    public static DataSource getDataSource() {
        return pool;
    }

    public static void closePool() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
    }
}
