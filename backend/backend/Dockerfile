# Use OpenJDK 17 (Render supports this version)
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy Maven/Gradle project files
COPY . .

# Package the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Expose the port your app runs on
EXPOSE 8081

# Run the JAR
CMD ["java", "-jar", "target/UPI_payment-0.0.1-SNAPSHOT.jar"]
