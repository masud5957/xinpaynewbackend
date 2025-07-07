# ---------- Stage 1: Build ----------
FROM maven:3.9.3-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY backend/pom.xml /app/backend/
WORKDIR /app/backend
RUN mvn dependency:go-offline

# Now copy the actual source
COPY backend /app/backend

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/backend/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
