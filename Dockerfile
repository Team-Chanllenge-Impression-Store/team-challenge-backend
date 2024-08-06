FROM openjdk:17

COPY . .

ENTRYPOINT ["java","-jar","/target/demo-0.0.1-SNAPSHOT.jar"]