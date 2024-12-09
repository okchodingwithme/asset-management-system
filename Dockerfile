# Use the official Gradle image as a build stage
FROM gradle:8.7 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle ./
COPY gradle gradle/
COPY src src/

# Build the application
RUN gradle clean bootJar --no-daemon

# Use a lightweight JRE for the runtime stage
FROM openjdk:21

# Set the working directory for the runtime
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application's port (default Spring Boot port)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]