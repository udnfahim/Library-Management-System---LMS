# =========================
# Build stage
# =========================
FROM amazoncorretto:25 AS build

# Set working directory
WORKDIR /app

# Install Maven (Amazon Corretto image doesn't include Maven)
RUN yum install -y maven && yum clean all

# Copy all project files
COPY . .

# Build the application and skip tests for faster builds
RUN mvn clean package -DskipTests

# =========================
# Run stage
# =========================
FROM amazoncorretto:25

# Set a non-root user for security
RUN useradd -m lmsuser
USER lmsuser

# Set working directory
WORKDIR /app

# Copy the compiled JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Optional: environment variables for database (can be overridden by docker-compose)
ENV DB_HOST=db \
    DB_PORT=3306 \
    DB_NAME=libraryhub \
    DB_USER=lmsuser \
    DB_PASSWORD=lmspass

# Health check (correct port)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD curl -f http://localhost:8080/ || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
