# Tiering Machine Server

A dystopian Social Credit System simulation built with Spring Boot.

## Overview

The backend service for the Tiering Machine, handling the core logic, database interactions, and AI integration.

## Key Features

- **Social Credit System**: Manages citizen scores and tiers.
- **AI-Powered Judgement**: Integrates with Groq API (Llama 3.1) to evaluate reports.
- **Betting System**: Manages bets.
- **Event System**: Handles system events.
- **Surveillance**: Real-time monitoring via `SurveillanceStreamHandler`.

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.2
- **Database (Cloud)**: Azure Database for **PostgreSQL** flexible servers
- **AI Integration**: Groq API (Llama 3.1 8B)
- **Build Tool**: Gradle (Kotlin DSL)
- **Containerization**: Docker

## Configuration

### Environment Variables

The application requires the following configuration (e.g., in `application.properties` or environment variables):

- `groq.api.key`: API key for Groq AI service.
- Database connection details (URL, username, password).

### CORS

CORS is configured to allow requests from:

- `https://l145.be`
- `http://localhost:5173` (Local development)

## Running the Application

### Using Gradle

Build the jar:

```bash
./gradlew bootJar # or pwsh .\build-server.ps1
```

Run the jar:

```bash
./gradlew bootRun # or pwsh .\start-server.ps1
```

### Using Docker

Build the image:

```bash
docker build -t tiering-machine-server . # or pwsh .\docker-build.ps1
```

Run the container:

```bash
docker run -p 8080:8080 --env-file .env --name tiering-machine-instance tiering-machine-server # or pwsh .\docker-run.ps1
```

## API Documentation

The application includes Swagger UI for API documentation. Once running, access it at:
`http://localhost:8080/swagger-ui.html` (Default SpringDoc path)

## Troubleshooting

### bootJar fails

- OneDrive

  OneDrive may be blocking the build process. Try disabling OneDrive or moving the project to a different location. If you do not want to move it, run these commands in sequence:

  ```bash
  ./gradlew --stop
  Stop-Process -Name "java" -Force
  Remove-Item -Recurse -Force .\build\
  ./gradlew bootJar
  ```
