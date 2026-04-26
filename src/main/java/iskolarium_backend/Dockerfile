# 1. Use Java 25 to match your laptop's environment
FROM eclipse-temurin:25-jdk

# 2. Create a folder inside the cloud server called /app
WORKDIR /app

# 3. Copy all your project files into that folder
COPY . .

# 4. Give the Maven Wrapper permission to run
RUN chmod +x ./mvnw

# 5. Build the .jar file
RUN ./mvnw clean package -DskipTests

# 6. Turn the server on!
CMD ["java", "-jar", "target/iskolarium_backend-0.0.1-SNAPSHOT.jar"]