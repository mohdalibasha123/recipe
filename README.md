# Recipe Application – Spring Boot Backend

## Introduction

The Recipe Application is a **Spring Boot-based backend API** that:

- Searches for recipes based on various parameters.
- Retrieves detailed recipe information, including **total calorie calculations**.
- Fetches nutritional information for recipes and supports **excluding specific ingredients**.

This backend:

- Is built with **Java 17** and **Spring Boot**.
- Follows RESTful API best practices.
- Acts as a **secure middleware** between the frontend (e.g. React) and the **Spoonacular API**.
- Keeps the **Spoonacular API key** on the server side and never exposes it to the client.


## GitHub Repository

- Backend repository: [https://github.com/mohdalibasha123/recipe](https://github.com/mohdalibasha123/recipe)
- FrontEnd repository: [https://github.com/mohdalibasha123/recipe-ui](https://github.com/mohdalibasha123/recipe-ui)


---

## Features

- Search recipes with custom filters and pagination.
- Retrieve detailed recipe information including total calories.
- Exclude ingredients from the calorie calculation via a dedicated endpoint.
- Centralized error handling and validation.
- Integrated **Swagger UI** for exploring and testing the API.
- Dockerized (multi-stage) build for consistent runtime across environments.

---

## Setup Instructions

This section covers **all steps** needed to run the backend on **Windows**, **Linux**, and **macOS**, using either:

- **Docker**, or
- **Manual run** with Maven and Java.

### 1. Prerequisites

Make sure you have:

- **Java 17**
- **Maven** (if running manually)
- **Docker** (if running via Docker)
- A valid **Spoonacular API key**

The file `src/main/resources/application.properties` uses an environment variable for the API key:

```properties
spoonacular.api_key=${SPOONACULAR_API_KEY}
```

> Spring Boot will read the key from the `SPOONACULAR_API_KEY` environment variable.  
> You must set this variable **before** starting the application (or pass it into Docker).

---

### 2. Set `SPOONACULAR_API_KEY` (Windows, Linux, macOS)

You need to set the `SPOONACULAR_API_KEY` environment variable so that Spring can resolve `spoonacular.api_key=${SPOONACULAR_API_KEY}`.

#### 2.1 Windows

**PowerShell:**

```powershell
$env:SPOONACULAR_API_KEY="your-real-spoonacular-key"
```

**Command Prompt (CMD):**

```cmd
set SPOONACULAR_API_KEY=your-real-spoonacular-key
```

> Run `mvn`, `java`, or `docker run` from the **same terminal** where you set the variable, or configure it in the system environment variables for a permanent setup.

---

#### 2.2 Linux (bash/zsh)

In your terminal:

```bash
export SPOONACULAR_API_KEY="your-real-spoonacular-key"
```

To make it persistent across sessions, add the line above to `~/.bashrc`, `~/.zshrc`, or your shell profile.

---

#### 2.3 macOS (Terminal)

In Terminal:

```bash
export SPOONACULAR_API_KEY="your-real-spoonacular-key"
```

As on Linux, add it to your shell profile if you want it to persist.

---

#### 2.4 (Optional) IDE Run Configuration

If running from an IDE (e.g. IntelliJ):

- Add `SPOONACULAR_API_KEY` in the **Run/Debug configuration** → Environment variables:
    - Name: `SPOONACULAR_API_KEY`
    - Value: `your-real-spoonacular-key`

Spring will pick it up automatically.

---

### 3. Build and Run Using Docker (All OS)

You can run the backend as a Docker container using the provided multi-stage Dockerfile:

```dockerfile
# Stage 1: Build the JAR file
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Build the runtime image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/recipe-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 3.1 Build the Docker image

From the project root (where `pom.xml` and `Dockerfile` are located):

```bash
docker build -t recipe-app .
```

---

#### 3.2 Run the Docker container with the API key

Run the container and pass the `SPOONACULAR_API_KEY` environment variable:

```bash
docker run -p 8080:8080   -e SPOONACULAR_API_KEY="your-real-spoonacular-key"   recipe-app
```

Notes:

- On Linux/macOS this command works as-is.
- On Windows PowerShell, you can use the same command or put it on one line if escaping is an issue.
- Inside the container, `SPOONACULAR_API_KEY` is mapped to `spoonacular.api_key` via `application.properties`.

---

### 4. Manual Build and Run (All OS, without Docker)

You can also run the Spring Boot app directly using Maven and Java.

#### 4.1 Build the application JAR

From the project root:

```bash
mvn clean package
```

This compiles the project and creates:

```text
target/recipe-0.0.1-SNAPSHOT.jar
```

Make sure `SPOONACULAR_API_KEY` is set (see section 2) **before** running the next step.

---

#### 4.2 Run the Spring Boot application

Run the JAR with Java 17:

```bash
java -jar target/recipe-0.0.1-SNAPSHOT.jar
```

This command is the same on **Windows**, **Linux**, and **macOS**, as long as Java 17 is on the PATH.

Spring Boot will:

- Resolve `spoonacular.api_key=${SPOONACULAR_API_KEY}`.
- Use the Spoonacular key from the environment variable you set.

---

### 5. Verify the API is Running

Once the application is up (via Docker or manual run), verify with:

- **Base URL**  
  `http://localhost:8080`

- **Swagger UI**  
  `http://localhost:8080/swagger-ui.html`

- **OpenAPI JSON**  
  `http://localhost:8080/api-docs`

A **Postman collection** is included in the main project folder with sample requests and responses for different scenarios.

---

## Design Decisions

### 1. Layered Architecture

The backend uses a layered architecture:

- **Controller layer**
    - Exposes REST endpoints under `/api/recipes/...`.
    - Validates incoming requests and returns standardized responses.
- **Service layer**
    - Implements business logic:
        - Calls the Spoonacular client.
        - Applies rules such as ingredient exclusions and calorie calculations.
- **Client (REST) layer**
    - Encapsulates all calls to the Spoonacular API.
    - Constructs URLs, attaches the API key, and maps responses to internal DTOs.
- **Model/DTO layer**
    - Defines `RecipeSearchReq`, `RecipeInformationReq`, `RecipeNutritionInformationReq`, `RecipeSearchRes`, `RecipeDto`, `Nutrition`, etc.
    - Decouples external API models from internal API contracts.

This separation improves:

- Maintainability.
- Testability (each layer can be tested individually).
- Clarity when adding new features or changing external integrations.

---

### 2. Secure API Key Handling (Backend as Middleware)

Key design choices for security:

- The Spoonacular API key is **not hardcoded** in code.
- It is externalized as:
    - `spoonacular.api_key=${SPOONACULAR_API_KEY}` in `application.properties`.
    - The environment variable `SPOONACULAR_API_KEY` set at runtime.
- Only the **backend** ever sees the key:
    - The React frontend never receives it.
    - It is not logged or exposed via Swagger.

This ensures:

- Secrets are not committed to version control.
- Different environments (dev/test/prod) can use different keys without code changes.
- The backend safely acts as a **middleware** between the client and Spoonacular.

---

### 3. Swagger / OpenAPI Integration

The project integrates **springdoc-openapi** to automatically generate:

- **Swagger UI** at `/swagger-ui.html`.
- **OpenAPI specification** at `/api-docs`.

Advantages:

- API documentation stays in sync with the code.
- Reviewers and frontend developers can explore endpoints and payloads easily.
- Quick manual testing without needing a separate client.

---

### 4. API Design and Validation

The API is designed to be:

- **RESTful** and stateless.
- **Clear** in its endpoints and parameters.

Examples:

- `GET /api/recipes/search`
- `GET /api/recipes/information`
- `GET /api/recipes/nutrition/info`

Validation:

- Uses `@Valid` on controller method parameters.
- Bean validation annotations like `@Min` to enforce value ranges.
- Enums like `SortOption`, `SortDirection`, `Intolerances` for controlled inputs.

This enforces:

- Early rejection of invalid input.
- Predictable behavior for consumers.
- Reduced risk of unexpected runtime errors.

---

### 5. Centralized Error Handling

The application uses:

- `@ControllerAdvice` to capture exceptions globally.
- Custom exception types and standardized error responses.

Goals:

- Provide consistent error JSON across all endpoints.
- Hide low-level technical details from API consumers.
- Detect and translate Spoonacular-specific errors (e.g. HTTP 402 for daily limit exceeded) into **clear messages** that the frontend can display.

---

### 6. Multi-Stage Dockerfile

The Dockerfile follows a **multi-stage** pattern:

1. **Builder stage**
    - Uses `maven:3.9.4-eclipse-temurin-17`.
    - Runs `mvn clean package`.
    - Produces the runnable JAR.

2. **Runtime stage**
    - Uses a slim `openjdk:17-jdk-alpine` image.
    - Copies the built JAR.
    - Exposes port `8080` and runs `java -jar app.jar`.

Benefits:

- Smaller final image (no Maven or build tools).
- Clear separation of build and runtime concerns.
- Portable, production-friendly container.

---

## AI Usage Reflection

The assignment requires **AI integration**, including using AI for boilerplate, challenging AI for optimized solutions, and documenting focus areas, challenges, and lessons learned.

### How AI Was Used

AI was used to:

- Generate initial **skeletons** for:
    - Controllers, services, and DTOs.
    - Error handling patterns using `@ControllerAdvice`.
- Propose a **multi-stage Dockerfile** for Maven + Spring Boot.
- Suggest patterns for securing configuration:
    - Using environment variables and `${SPOONACULAR_API_KEY}`.
- Draft and structure this **README**:
    - Setup instructions across OS and run modes.
    - Design decisions explanation.
    - AI reflection and lessons learned.

This reduced time spent on repetitive setup and documentation tasks.

---

### How AI Output Was Challenged and Validated

AI-generated ideas were **not accepted blindly**. They were reviewed and adjusted:

- **Security & configuration**
    - AI examples that suggested hardcoded keys were rejected.
    - The final approach uses environment variables and property placeholders to avoid committing secrets.

- **DTO and API contract**
    - AI assumed generic response structures.
    - DTOs were verified and aligned with the actual Spoonacular API and the project’s real response requirements.

- **Error handling**
    - Generic error-handling patterns were adapted to:
        - Recognize Spoonacular’s 402 “daily points limit” errors.
        - Return user-friendly messages to the frontend instead of raw error text.

- **Docker**
    - The multi-stage Dockerfile from AI was tested.
    - The final file ensures that:
        - The correct JAR (`recipe-0.0.1-SNAPSHOT.jar`) is copied.
        - The container exposes port `8080`.
        - The application starts correctly in all environments.

In short, AI functioned as a **productivity helper**, while architecture, security, and behavior were decided and validated through manual reasoning and testing.

---

## Lessons Learned and Highlights

### Lessons Learned

1. **Secrets must be externalized early**  
   Using `SPOONACULAR_API_KEY` and `spoonacular.api_key=${SPOONACULAR_API_KEY}` keeps sensitive data out of the codebase and simplifies deployment across environments.

2. **Layered design pays off**  
   Separating concerns into controllers, services, REST clients, and DTOs makes the project easier to understand and evolve.

3. **Error handling is part of the API design**  
   Handling external API limits and network issues gracefully creates a better experience for frontend developers and end users.

4. **Docker multi-stage builds are very effective**  
   Building the JAR in one image and running it in a slimmer runtime image leads to leaner, more maintainable containers.

5. **AI is helpful but must be verified**  
   AI speeds up boilerplate and documentation but:
    - Must be checked against real endpoints, tools, and runtime behavior.
    - Cannot replace human judgment for security, architecture, and error handling decisions.

---

## Highlights

- A **secure, layered Spring Boot backend** that:
    - Keeps the Spoonacular API key out of source control.
    - Exposes clear, well-documented endpoints for the frontend.
    - Translates external API errors into meaningful, user-friendly responses.
- Cross-platform **setup instructions** for:
    - Windows, Linux, and macOS.
    - Both Docker-based and manual Java/Maven workflows.
- A backend ready to serve as a robust middleware layer in the full-stack Recipe Application, aligned with the assignment requirements:
    - **Setup instructions**
    - **Design decisions**
    - **AI usage reflection**
    - **Lessons learned and highlights**