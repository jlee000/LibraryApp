# Use an official Java runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /LibraryApp

# Copy the jar file from target into the container
ARG JAR_FILE=target/LibraryApp.jar
COPY ${JAR_FILE} /app/app.jar

# Ensure the file has execution permissions (optional)
RUN chmod +x /app/app.jar

# Expose the port the app will run on
EXPOSE 8080

# Define the default command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
