package com.group7.straytostay.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Holds the one HikariCP connection pool for the application (Singleton).
 * Settings come from environment variables so no credential is in the code:
 * DB_URL, DB_USER, DB_PASSWORD, DB_POOL_SIZE (optional, default 10).
 */
public final class DataSourceProvider {

    private static final Logger log = LoggerFactory.getLogger(DataSourceProvider.class);
    private static volatile DataSourceProvider instance;

    private final HikariDataSource dataSource;

    private DataSourceProvider() {
        HikariConfig config = new HikariConfig();
        // Name the driver explicitly: inside Tomcat the driver in WEB-INF/lib is not always
        // registered with DriverManager before this listener runs.
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(env("DB_URL", "jdbc:postgresql://localhost:5432/straytostay"));
        config.setUsername(env("DB_USER", "straytostay"));
        config.setPassword(env("DB_PASSWORD", "straytostay"));
        config.setMaximumPoolSize(Integer.parseInt(env("DB_POOL_SIZE", "10")));
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10_000);
        config.setPoolName("straytostay-pool");
        this.dataSource = new HikariDataSource(config);
        log.info("Connection pool started for {}", config.getJdbcUrl());
    }

    public static DataSourceProvider getInstance() {
        if (instance == null) {
            synchronized (DataSourceProvider.class) {
                if (instance == null) {
                    instance = new DataSourceProvider();
                }
            }
        }
        return instance;
    }

    /** Borrows a connection from the pool. Always close it (try-with-resources) to return it. */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        DataSourceProvider current = instance;
        if (current != null) {
            current.dataSource.close();
            instance = null;
            log.info("Connection pool closed");
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
