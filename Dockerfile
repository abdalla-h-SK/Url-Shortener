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








## ==========================================
## Stage 1: Build the application
## ==========================================
#FROM maven:3.9-eclipse-temurin-17 AS builder
#WORKDIR /app
#
## Copy dependency definition files first to leverage Docker layer caching
#COPY pom.xml .
#COPY .mvn .mvn
#COPY mvnw .
## If you aren't using the Maven wrapper, comment out the line above and below
## and use standard 'mvn' commands.
#RUN chmod +x mvnw
#
## Download dependencies (cached if pom.xml doesn't change)
#RUN ./mvnw dependency:go-offline
#
## Copy the rest of the source code
#COPY src ./src
#
## Build the fat JAR, skipping tests for faster builds
#RUN ./mvnw clean package -DskipTests
#
## ==========================================
## Stage 2: Runtime image
## ==========================================
#FROM eclipse-temurin:17-jre-jammy
#WORKDIR /app
#
## Create a non-root user for security best practices
#RUN groupadd -g 1000 spring && useradd -u 1000 -g spring -s /bin/sh spring
#USER spring:spring
#
## Copy the compiled JAR from the builder stage
#COPY --from=builder /app/target/*.jar app.jar
#
## Expose the default Spring Boot port
#EXPOSE 8080
#
## Run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]