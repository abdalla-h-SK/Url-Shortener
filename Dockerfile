# Use a lightweight Java 17 runtime environment (no Maven or JDK needed)
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR directly from the runner's target folder into the container
COPY target/*.jar app.jar

# Expose your application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]