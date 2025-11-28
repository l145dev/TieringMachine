# Stage 1: Build Stage
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and config files first to leverage Docker layer caching
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Ensure gradlew is executable
RUN chmod +x gradlew

# Download dependencies (cached if build files don't change)
RUN ./gradlew dependencies --no-daemon

# Copy the actual source code
COPY src src

# Build the JAR
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime Stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Expose the default Spring Boot port
EXPOSE 8080

# Copy the built artifact
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]