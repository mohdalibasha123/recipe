# ============================
# Stage 1: Build the JAR file
# ============================
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy Maven descriptor and sources
COPY pom.xml .
COPY src ./src

# Build the application (skip tests to speed up if desired)
RUN mvn -B -DskipTests clean package


# ===================================
# Stage 2: Build the runtime image
# ===================================
FROM eclipse-temurin:17-jdk

# Set working directory for the runtime container
WORKDIR /app

# Copy the built JAR from the builder stage
# This will match any JAR produced in target/ (e.g. recipe-0.0.1-SNAPSHOT.jar)
ARG JAR_FILE=target/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8080

# Optional: JVM options can be customized via JAVA_OPTS env var
ENV JAVA_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
