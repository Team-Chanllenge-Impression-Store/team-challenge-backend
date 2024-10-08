FROM openjdk:17

WORKDIR /app

COPY .mvn/ .mvn

COPY mvnw pom.xml ./

# dir with *.java files
COPY src ./src

# skip tests for now
RUN mvn package -DskipTests

# copy local configuration file into container if needed
CMD ["./mvnw", "spring-boot:run"]