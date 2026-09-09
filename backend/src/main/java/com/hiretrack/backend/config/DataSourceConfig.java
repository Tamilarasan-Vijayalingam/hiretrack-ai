package com.hiretrack.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.url:${DATABASE_URL:jdbc:postgresql://localhost:5432/hiretrack}}")
    private String rawUrl;

    @Value("${spring.datasource.username:${DATABASE_USER:postgres}}")
    private String defaultUser;

    @Value("${spring.datasource.password:${DATABASE_PASSWORD:postgres}}")
    private String defaultPass;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        String jdbcUrl = rawUrl;
        String username = defaultUser;
        String password = defaultPass;

        // Automatically convert Render/Heroku postgres:// or postgresql:// URLs into valid JDBC connection format
        if (rawUrl != null && (rawUrl.startsWith("postgres://") || rawUrl.startsWith("postgresql://"))) {
            try {
                URI uri = new URI(rawUrl);
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();

                String query = uri.getQuery() != null ? "?" + uri.getQuery() : "";
                jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path + query;

                if (uri.getUserInfo() != null && uri.getUserInfo().contains(":")) {
                    String userInfo = uri.getUserInfo();
                    int colonIndex = userInfo.indexOf(':');
                    username = userInfo.substring(0, colonIndex);
                    password = userInfo.substring(colonIndex + 1);
                }
                log.info("Normalized PostgreSQL connection URL to JDBC format: {}", jdbcUrl);
            } catch (Exception e) {
                log.warn("Could not parse PostgreSQL URI, attempting standard connection: {}", e.getMessage());
            }
        }

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        // Production-tuned connection pool
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(20000);

        return new HikariDataSource(config);
    }
}
