# Recipe Application – Spring Boot Backend

## Introduction

The Recipe Application is a **Spring Boot-based backend API** that:

- Searches for recipes based on various parameters.
- Retrieves detailed recipe information, including **total calorie calculations**.
- Fetches nutritional information for recipes and supports **excluding specific ingredients**.

This backend:

- Is built with **Java 17** and **Spring Boot**.
- Follows RESTful API best practices.
- Acts as a **secure middleware** between the frontend (React) and the **Spoonacular API**.
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
- Containerized backend using Docker for consistent runtime across environments.

---

## Setup Instructions

This section covers **all steps** needed to run the backend on **Windows**, **Linux**, and **macOS**, using **Docker as the recommended way**.  
An optional manual method with Java 17 + Maven is also described.

The backend expects the Spoonacular API key to be provided via an environment variable:

```properties
spoonacular.api_key=${SPOONACULAR_API_KEY}
```

---

## Quick Start – Run with Docker (Recommended for All OS)

These steps work on **Windows**, **macOS**, and **Linux**.

### 1. Prerequisites

- **Docker** installed and running:
    - Windows / macOS: Docker Desktop.
    - Linux: Docker Engine (via your distribution's package manager).
- A valid **Spoonacular API key**.
- The backend project cloned locally (this repository).

### 2. Clone the repository

```bash
git clone <YOUR_BACKEND_REPO_URL>
cd <BACKEND_PROJECT_FOLDER>
```

Replace:

- `<YOUR_BACKEND_REPO_URL>` with your GitHub repository URL.
- `<BACKEND_PROJECT_FOLDER>` with the backend project folder name.

> The `Dockerfile` is already included in the backend project root (same folder as `pom.xml`).

### 3. Build the Docker image

From the backend project root:

```bash
docker build -t recipe-backend .
```

This will:

- Use Maven inside a container to run `mvn clean package`.
- Build the Spring Boot JAR file.
- Create a final Docker image named `recipe-backend` with Java 17 and your application.

### 4. Run the Docker container with your API key

```bash
docker run -p 8080:8080   -e SPOONACULAR_API_KEY="your-real-spoonacular-key"   recipe-backend
```

Notes:

- `-p 8080:8080` maps container port **8080** to host port **8080**.
- `-e SPOONACULAR_API_KEY=...` passes your Spoonacular API key into the container.
- On Windows PowerShell / CMD you can also run this as a single line (no `\`):

  ```bash
  docker run -p 8080:8080 -e SPOONACULAR_API_KEY="your-real-spoonacular-key" recipe-backend
  ```

### 5. Verify the API is running

Once the container is running, you can access:

- **Base URL**:  
  `http://localhost:8080`
- **Swagger UI**:  
  `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**:  
  `http://localhost:8080/api-docs`

You can open the Swagger UI in a browser and test the endpoints directly.

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
    - Defines request and response objects such as:
        - `RecipeSearchReq`, `RecipeInformationReq`, `RecipeNutritionInformationReq`
        - `RecipeSearchRes`, `RecipeDto`, `Nutrition`, etc.
    - Decouples external API models from internal API contracts.

This structure improves:

- Maintainability.
- Testability (each layer can be tested independently).
- Clarity when adding new features or adjusting external integrations.

---

### 2. Secure API Key Handling (Backend as Middleware)

To protect the Spoonacular API key:

- The key is **not hardcoded** in the source code.
- It is provided as an environment variable:

    - In `application.properties`:
      ```properties
      spoonacular.api_key=${SPOONACULAR_API_KEY}
      ```

    - At runtime via:
        - Docker environment (`-e SPOONACULAR_API_KEY=...`), or
        - OS/terminal environment variable.

- The React frontend never sees the key and never talks directly to Spoonacular.

This ensures:

- Secrets are not committed to version control.
- Different environments (dev/test/prod) can use different keys without code changes.
- The backend safely acts as a **secure proxy/middleware** between the client and Spoonacular.

---

### 3. Swagger / OpenAPI Integration

The backend uses **springdoc-openapi** to automatically generate:

- **Swagger UI** at `/swagger-ui.html`.
- **OpenAPI specification** at `/api-docs`.

Benefits:

- API documentation is always in sync with the code.
- Easy exploration of endpoints and models.
- Simple manual testing without additional tools.

---

### 4. API Design and Validation

The API is designed to be:

- **RESTful** and stateless.
- Straightforward in naming and semantics.

Examples:

- `GET /api/recipes/search` – search for recipes.
- `GET /api/recipes/information` – get detailed recipe information.
- `GET /api/recipes/nutrition/info` – get nutrition data with option to exclude ingredients.

Validation:

- `@Valid` on controller parameters.
- Bean Validation annotations like `@Min` for numeric constraints.
- Enums (`SortOption`, `SortDirection`, `Intolerances`, etc.) for safe, controlled values.

This leads to:

- Early rejection of invalid input.
- Consistent and predictable API behavior.
- Reduced runtime errors.

---

### 5. Centralized Error Handling

The backend uses:

- `@ControllerAdvice` for global exception handling.
- Custom exception types for business and integration errors.

Goals:

- Provide consistent error JSON across endpoints.
- Hide internal stack traces and technical details from clients.
- Translate Spoonacular-specific errors (e.g., HTTP 402 “daily points limit exceeded”) into **clear, user-friendly messages** that the frontend can display.

---

### 6. Dockerized Runtime

The application is packaged into a Docker image using a **multi-stage build**:

- Build stage:
    - Uses Maven and Java 17 inside a container to compile and package the app.
- Runtime stage:
    - Uses a Java 17 runtime image to run the built JAR.
    - Exposes port 8080.

Benefits:

- Consistent runtime across Windows, Linux, and macOS.
- No requirement for reviewers to install Java or Maven locally.
- Simple startup using only Docker and an environment variable.

---

## AI Usage Reflection

As required by the assignment, AI was used as a helper for repetitive and boilerplate tasks, while all important decisions were reviewed and validated.

### How AI Was Used

AI was used to:

- Draft initial versions of:
    - Controller/service/DTO structures.
    - Error handling patterns using `@ControllerAdvice`.
- Suggest a multi-stage Docker strategy for:
    - Building the JAR in one stage.
    - Running it in a lighter runtime stage.
- Propose configuration patterns using:
    - Environment variables (`SPOONACULAR_API_KEY`).
    - Spring Boot property placeholders.
- Help structure this README:
    - Setup steps for Docker and manual runs.
    - Design decisions.
    - Reflection and lessons learned.

This sped up boilerplate and documentation work.

---

### How AI Output Was Reviewed and Improved

AI suggestions were **always validated and adapted**:

- **Security**:
    - Any suggestion to hardcode API keys or secrets was rejected.
    - Final approach uses environment variables exclusively.

- **Contracts and DTOs**:
    - DTOs and responses were aligned with the actual Spoonacular API and business requirements, not just AI assumptions.

- **Error handling**:
    - Generic patterns were refined to:
        - Recognize Spoonacular’s 402 quota error.
        - Produce user-friendly error structures for the frontend.

- **Docker integration**:
    - The Docker approach was tested on real machines.
    - Ensured the image starts consistently across OSes using only Docker.

AI was treated as a **productivity tool**, not an authority: final decisions and behavior are based on deliberate design and testing.

---

## Lessons Learned and Highlights

### Lessons Learned

1. **Externalizing secrets is essential**  
   Using `SPOONACULAR_API_KEY` and mapping it via `spoonacular.api_key=${SPOONACULAR_API_KEY}` avoids leaking secrets into the codebase and makes deployments flexible.

2. **Layered architecture makes changes easier**  
   Controllers, services, REST clients, and DTOs each have clear roles, which simplifies debugging and adding features.

3. **Error handling is part of the API design**  
   Centralized, consistent error responses greatly improve the developer experience and reduce confusion on the frontend.

4. **Docker removes environment friction**  
   Reviewers can run the backend on any OS with a single `docker build` + `docker run` flow, with no need to align Java versions.

5. **AI is powerful but must be supervised**  
   It helps with scaffolding and documentation, but every suggestion must be checked against real requirements, security concerns, and runtime behavior.

---

### Highlights

- A **secure, layered Spring Boot backend** that:
    - Hides the Spoonacular API key.
    - Exposes clean, well-documented REST endpoints.
    - Offers meaningful error messages to the frontend.
- A **Docker-first** run strategy:
    - Same steps for Windows, macOS, and Linux.
    - Minimal setup friction for reviewers.
- Documentation aligned with the assignment requirements:
    - **Setup instructions**
    - **Design decisions**
    - **AI usage reflection**
    - **Lessons learned and highlights**
