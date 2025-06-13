# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download all dependencies. This layer is cached unless pom.xml changes.
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping tests for faster build
# The JAR will be created in /app/target/
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
# Use a slim JRE image for a smaller final image size
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Argument to specify the JAR file name (optional, with a default)
ARG JAR_FILE_NAME=ineni_pt-0.0.1-SNAPSHOT.jar

# Copy the JAR file from the build stage to the /app directory in the new stage
# Adjust the JAR_FILE_NAME below if your artifactId or version in pom.xml is different
COPY --from=build /app/target/${JAR_FILE_NAME} app.jar

# Expose the port the application runs on (from your application.properties)
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]