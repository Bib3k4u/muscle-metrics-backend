# Use OpenJDK image
FROM openjdk:17-jdk-slim

# Add a label
LABEL maintainer="your_email@example.com"

# Set the working directory
WORKDIR /app

# Copy JAR from target folder
COPY target/*.jar muscle-metrics-0.0.1-SNAPSHOT.jar

# Expose a port (optional, typically 8080)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "muscle-metrics-0.0.1-SNAPSHOT.jar"]
