# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copia o WAR gerado pelo Maven e renomeia para app.war
COPY --from=build /app/target/*.war app.war

# Otimizações para Render
ENV JAVA_OPTS="-Xmx512m -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080

# CORREÇÃO CRÍTICA AQUI:
# 1. Usando a forma "shell" do ENTRYPOINT (sem colchetes e aspas) para que $JAVA_OPTS seja expandido.
# 2. Referenciando o arquivo RENOMEADO para 'app.war'.
ENTRYPOINT java $JAVA_OPTS -jar app.war