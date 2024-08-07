FROM openjdk:17

WORKDIR /app

COPY .mvn/ .mvn

COPY mvnw pom.xml ./

# dir with *.java files
COPY src ./src

# copy local configuration file into container if needed

CMD ["./mvnw", "spring-boot:run"]