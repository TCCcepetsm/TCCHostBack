# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.war app.war

# Configurações do PostgreSQL (substitua pelas suas variáveis de ambiente)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/recorder_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]