FROM maven:3.8.6-openjdk-18 AS build

WORKDIR /app

COPY pom.xml ./

COPY src ./src

RUN mvn package -DskipTests

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run"]