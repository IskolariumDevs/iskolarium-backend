# match laptop's environment
FROM eclipse-temurin:25-jdk

# create a folder inside the cloud server called /app
WORKDIR /app

# copy all your project files into that folder
COPY . .

# give the maven wrapper permission to run
RUN chmod +x ./mvnw

# build the .jar file
RUN ./mvnw clean package -DskipTests

# turn the server on
CMD ["sh", "-c", "java -jar target/*.jar"]