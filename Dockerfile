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

# Otimizações para Render
ENV JAVA_OPTS="-Xmx512m -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080

# Não defina variáveis de banco aqui (use as do Render)
ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "app.war"]