FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY file-sharing/pom.xml file-sharing/mvnw ./
COPY file-sharing/.mvn .mvn

RUN ./mvnw dependency:go-offline

COPY file-sharing/src ./src

RUN ./mvnw package -DskipTests

# Run phase
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
