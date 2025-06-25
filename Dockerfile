# Use a JDK image to build the project
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# Copy project files
COPY . .

# Build the application (fat JAR)
RUN gradle clean installDist

# Use a lightweight image to run the app
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built files
COPY --from=build /app/build/install/* /app/

# Expose port (customize if your app uses a different one)
EXPOSE 8081

# Run the application
CMD ["./bin/tanvisphysiocare"]
