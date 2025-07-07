package com.recorder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class RecorderSrcApplicationTests {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Test
    void contextLoads() {
        // Teste básico de inicialização
        assertTrue(postgreSQLContainer.isRunning(), "O container PostgreSQL deve estar rodando");
    }

    @Test
    void testDatabaseConnection() {
        // Teste adicional para verificar conexão com o banco
        assertTrue(postgreSQLContainer.isCreated(), "O container deve ter sido criado");
        assertTrue(postgreSQLContainer.isRunning(), "O container deve estar em execução");
    }
}