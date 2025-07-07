# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Esta linha COPIA o WAR gerado pelo Maven e o RENOMEIA para app.war
COPY --from=build /app/target/*.war app.war

# Otimizações para Render
ENV JAVA_OPTS="-Xmx512m -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080

# Esta linha INICIA sua aplicação, usando o 'app.war'
# e expande JAVA_OPTS corretamente
ENTRYPOINT java $JAVA_OPTS -jar app.war