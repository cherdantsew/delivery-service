package com.deliverengine.deliver;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestPostgresContainer {

    @SuppressWarnings("rawtypes")
    private static PostgreSQLContainer postgreSQLContainer;

    public TestPostgresContainer() {
    }


    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.9-alpine");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
            .withReuse(true);

        postgreSQLContainer.start();
    }

    @SuppressWarnings("unused")
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry propertyRegistry) {

        String jdbcUrl = getPostgreSQLContainer().getJdbcUrl();

        propertyRegistry.add("integration-tests-db", getPostgreSQLContainer()::getDatabaseName);
        propertyRegistry.add("spring.datasource.username", getPostgreSQLContainer()::getUsername);
        propertyRegistry.add("spring.datasource.password", getPostgreSQLContainer()::getPassword);
        propertyRegistry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @SuppressWarnings("rawtypes")
    public static PostgreSQLContainer getPostgreSQLContainer() {
        return postgreSQLContainer;
    }
}
