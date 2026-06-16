package com.example.roseeducation.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class RenderDatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String PROPERTY_SOURCE_NAME = "renderDatabaseUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = firstNonBlank(
                environment.getProperty("SPRING_DATASOURCE_URL"),
                environment.getProperty("DATABASE_URL")
        );

        if (!hasText(databaseUrl)) {
            return;
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.datasource.url", toJdbcUrl(databaseUrl.trim()));

        Credentials credentials = extractCredentials(databaseUrl.trim());
        if (!hasText(environment.getProperty("SPRING_DATASOURCE_USERNAME")) && hasText(credentials.username())) {
            properties.put("spring.datasource.username", credentials.username());
        }
        if (!hasText(environment.getProperty("SPRING_DATASOURCE_PASSWORD")) && hasText(credentials.password())) {
            properties.put("spring.datasource.password", credentials.password());
        }

        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static String toJdbcUrl(String url) {
        String uriText = url.startsWith("jdbc:") ? url.substring("jdbc:".length()) : url;
        if (uriText.startsWith("postgresql://") || uriText.startsWith("postgres://")) {
            return toJdbcPostgresUrl(uriText);
        }
        return url;
    }

    private static String toJdbcPostgresUrl(String uriText) {
        try {
            URI uri = URI.create(uriText);
            String host = uri.getHost();
            if (!hasText(host)) {
                return fallbackJdbcPostgresUrl(uriText);
            }

            StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://").append(host);
            if (uri.getPort() != -1) {
                jdbcUrl.append(":").append(uri.getPort());
            }
            jdbcUrl.append(hasText(uri.getRawPath()) ? uri.getRawPath() : "/");
            if (hasText(uri.getRawQuery())) {
                jdbcUrl.append("?").append(uri.getRawQuery());
            }
            return jdbcUrl.toString();
        } catch (IllegalArgumentException ex) {
            return fallbackJdbcPostgresUrl(uriText);
        }
    }

    private static String fallbackJdbcPostgresUrl(String uriText) {
        String sanitized = uriText.replaceFirst("^(postgresql|postgres)://[^/@]+@", "$1://");
        if (sanitized.startsWith("postgresql://")) {
            return "jdbc:" + sanitized;
        }
        if (sanitized.startsWith("postgres://")) {
            return "jdbc:postgresql://" + sanitized.substring("postgres://".length());
        }
        return uriText;
    }

    private static Credentials extractCredentials(String url) {
        try {
            String uriText = url.startsWith("jdbc:") ? url.substring("jdbc:".length()) : url;
            URI uri = URI.create(uriText);
            String userInfo = uri.getRawUserInfo();
            if (!hasText(userInfo)) {
                return new Credentials(null, null);
            }

            String[] parts = userInfo.split(":", 2);
            String username = decode(parts[0]);
            String password = parts.length > 1 ? decode(parts[1]) : null;
            return new Credentials(username, password);
        } catch (IllegalArgumentException ex) {
            return new Credentials(null, null);
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String firstNonBlank(String first, String second) {
        return hasText(first) ? first : second;
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private record Credentials(String username, String password) {
    }
}
