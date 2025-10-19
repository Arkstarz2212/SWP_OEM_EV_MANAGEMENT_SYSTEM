# ============================================================
# Stage 1: Build the Spring Boot application
# ============================================================
FROM maven:3.9-eclipse-temurin-25 AS build

# Set working directory inside container
WORKDIR /app

# Copy Maven configuration first (for dependency caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw ./

# Make the Maven wrapper executable and pre-download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the JAR (skip tests for faster builds)
RUN ./mvnw clean package -DskipTests

# ============================================================
# Stage 2: Create a minimal runtime image
# ============================================================
FROM eclipse-temurin:25-jre-alpine

# Set working directory
WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Environment configuration (override at runtime if needed)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Optional health check (you can adjust endpoint)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
