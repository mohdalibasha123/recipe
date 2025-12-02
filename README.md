# **Introduction**

- The Recipe Application is a Spring Boot-based backend API designed to:
    - Search for recipes based on various parameters.
    - Retrieve detailed recipe information, including total calorie calculations.
    - Fetch nutritional information for recipes, allowing ingredient exclusions.

- The project is built using **Java 17**, **Spring Boot**, and follows best practices for RESTful API design.

---

# **Features**

- Search for recipes with custom filters.
- Retrieve recipe details, including calculated calorie counts.
- Exclude specific ingredients from calorie calculations.
- Integrated **Swagger UI** for easy API exploration and testing.

---

# **Prerequisites**

Before running the application, ensure the following tools are installed:

- **Docker**
- **Java 17** (if running manually)
- **Maven** (if running manually)

---

# **Setup and Run**

### **Step 1: Add API Key**
- Place the **Spoonacular API key** in the `application.properties` file located under the `src/main/resources/` directory.
    - For property: `spoonacular.api_key`.

---

### **Step 2: Build and Run Using Docker**

1. Build the Docker image by running the following command in the project directory:
   ```bash
   docker build -t recipe-app .
   ```

2. Run the Docker container:
   ```bash
   docker run -p 8080:8080 recipe-app
   ```

---

### **Step 3: Manual Build and Run**

1. **Build the Application**: Package the application as a JAR file:
   ```bash
   mvn clean package
   ```

2. **Run the Application**: Start the application with the following command:
   ```bash
   java -jar target/recipe-0.0.1-SNAPSHOT.jar
   ```

---

# Access the Application:
   * API Base URL: http://localhost:8080.
   * Swagger Documentation: http://localhost:8080/swagger-ui.html.
   * OpenAPI JSON: http://localhost:8080/api-docs
   * Postman Collection provided in main folder contain request/response samples on various cases.

---

---

# Design Decisions

### **1. Layered Architecture**
The project is structured into layers:
- **Controller**: Handles API requests and forwards them to the service layer.
- **Service**: Manages business logic and external API interactions.
- **Client Rest**: Handles third-party API communication.
- **Model**: Defines request and response structures.

This structure simplifies code maintenance and makes future updates easier by keeping responsibilities separate.

---

### **2. Swagger/OpenAPI Integration**
Swagger UI is integrated using `springdoc-openapi` to automatically generate API documentation.
This provides developers with clear and up-to-date information about available endpoints, 
making it easier to collaborate and test the APIs.

---

### **3. API Design and Validation**
The APIs are designed to be stateless and user-friendly:
- Endpoints like `/search` and `/information` are simple and intuitive.
- Input validation is applied using annotations like `@Valid` to ensure data integrity and avoid errors in processing.

---

### **4. Error Handling**
Centralized error handling is implemented with `@ControllerAdvice` and custom exceptions.
This ensures consistent and meaningful error responses, making debugging and error resolution straightforward for API users.

---

### **5. Multi-Stage Dockerfile**
The Dockerfile uses a multi-stage build approach to streamline packaging. 
This reduces the image size by excluding unnecessary dependencies, resulting in a lightweight and efficient container ready for deployment.

---