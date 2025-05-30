#Build da aplicação
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

#Imagem final e start do .jar
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/foodsys-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]